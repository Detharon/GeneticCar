package com.dth.geneticcar;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import com.dth.geneticcar.datatype.CarChromosome;
import com.dth.geneticcar.datatype.CarIndividual;
import com.dth.geneticcar.datatype.FixtureSpecs;
import com.dth.geneticcar.datatype.AlgorithmSettings;
import com.dth.geneticcar.datatype.IndividualConstraints;
import com.dth.geneticcar.entities.CarChromosomeGenerator;
import com.dth.geneticcar.entities.CarEntity;
import com.dth.geneticcar.manager.CarEntityManager;
import com.dth.geneticcar.manager.CarIndividualManager;
import com.dth.geneticcar.utils.CarEvent;
import com.dth.geneticcar.utils.CarListener;
import com.dth.geneticcar.utils.ZoomProcessor;

public class Core implements ApplicationListener {
    private World world;

    public final int STATUS_READY = 0;
    public final int STATUS_PAUSED = 1;
    private int gameState;

    private final int CAMERA_FREE = 0;
    private final int CAMERA_FOLLOW = 1;
    private int cameraState = CAMERA_FOLLOW;

    public Box2DDebugRenderer debugRenderer;
    public ShapeRenderer shapeRenderer;
    public OrthographicCamera camera;
    public FPSLogger fpsLogger;
    public InputMultiplexer inputMultiplexer;

    static final float BOX_STEP = 1 / 60f;
    static final int BOX_VELOCITY_ITERATIONS = 6;
    static final int BOX_POSITION_ITERATIONS = 2;
    static float BOX_TO_WORLD = 100f;
    static final float ABOVE_GROUND = 1.5f;
    static final int FPS = 16; //60 fps: 1/60 = 0.0166 ms for each frame

    private float startingAltitude;
    private float finish;
    private float[] groundPoints;
    private boolean running = false;

    private AlgorithmSettings settings = new AlgorithmSettings();

    private ArrayList<CarListener> carListenerList = new ArrayList<CarListener>();

    Body ground;
    CarEntityManager carEntityManager;
    CarIndividualManager carIndividualManager;
    IndividualConstraints specs;

    public void setConstraints(IndividualConstraints specs) {
	this.specs = specs;
    }

    public void createCar(World world, String[] stringChromosome) {
	CarEntity[] cars = new CarEntity[stringChromosome.length];
	for (int i = 0; i < stringChromosome.length; i++) {
	    cars[i] = new CarEntity(world, 0.0f, startingAltitude + ABOVE_GROUND, new CarChromosome(stringChromosome[i]));
	}

	carEntityManager = new CarEntityManager(cars, finish);
	running = true;
    }

    public void createRandomCars(World world, int number) {
	specs = new IndividualConstraints(
	    6, 2,
	    0.2f, 1.0f,
	    0.1f, 0.25f,
	    new FixtureSpecs(), new FixtureSpecs(),
	    new FixtureSpecs(), new FixtureSpecs()
	);

	CarEntity[] cars = new CarEntity[number];

	for (int i = 0; i < number; i++) {
	    CarChromosomeGenerator gen = new CarChromosomeGenerator(
		specs.getChassisVertex(),
		specs.getMinChassisLength(), specs.getMaxChassisLength(),
		specs.getWheelVertex(),
		specs.getMinWheelRadius(), specs.getMaxWheelRadius(),
		new FixtureSpecs(), new FixtureSpecs(),
		new FixtureSpecs(), new FixtureSpecs()
	    );
	    CarChromosome chromosome = gen.generateCarChromosome();

	    cars[i] = new CarEntity(world, 0.0f, startingAltitude + ABOVE_GROUND, chromosome);
	}

	carEntityManager = new CarEntityManager(cars, finish);
	running = true;
    }

    public void createGround(float[] points) {
	if (ground != null) world.destroyBody(ground);
	groundPoints = points;

	finish = points[0] * points.length;

	FixtureDef groundFixtureDef = new FixtureDef();
	groundFixtureDef.density = 2.0f;
	groundFixtureDef.friction = .6f;

	BodyDef groundBodyDef = new BodyDef();
	groundBodyDef.position.set(new Vector2(0, 0.1f));

	Vector2[] vertices = new Vector2[points.length]; //number of track vertices
	startingAltitude = points[1]; //this needs to be remembered to properly create the cars
	vertices[0] = new Vector2(-10.f, startingAltitude); //track starts flat, so cars wont instantly fall down
	for (int i = 1; i < points.length; i++) {
	    /*
	     * points[0]*i is the X coordinate, starting from 1 vertex, because the 0th one is constant, not user defined
	     * points[i] is the Y coordinate
	     */
	    vertices[i] = new Vector2(points[0] * (i), points[i]);
	}

	ChainShape chain = new ChainShape();
	chain.createChain(vertices);

	groundFixtureDef.shape = chain;

	ground = world.createBody(groundBodyDef);
	ground.createFixture(groundFixtureDef);

	camera.position.set(camera.viewportWidth * .5f - 200, camera.viewportHeight * .5f + startingAltitude * BOX_TO_WORLD, 0f);
    }

    @Override
    public void create() {
	createWorld();
	fpsLogger = new FPSLogger();
	camera = new OrthographicCamera();
	camera.viewportHeight = 480;
	camera.viewportWidth = 720;
	camera.position.set(camera.viewportWidth * .5f - 200, camera.viewportHeight * .5f, 0f);
	camera.update();

	gameState = STATUS_READY;

	debugRenderer = new Box2DDebugRenderer();
	shapeRenderer = new ShapeRenderer();

	inputMultiplexer = new InputMultiplexer();
	inputMultiplexer.addProcessor(new ZoomProcessor(this));
	Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void createWorld() {
	if (world != null) {
	    world.dispose();
	}
	world = new World(new Vector2(0, -settings.getGravity()), true);
	world.setAutoClearForces(true);
	world.setContinuousPhysics(false);
    }

    @Override
    public void render() {
	long beg = System.currentTimeMillis();

	Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	Matrix4 cameraCopy = camera.combined.cpy();
	debugRenderer.render(world, cameraCopy.scl(BOX_TO_WORLD));

	if (gameState == STATUS_READY) {
	    if (running) {
		carEntityManager.evalDistances();
		running = carEntityManager.anyRunning();

		if (!running) {
		    // Creates individuals from physics models, models can be safely removed as they're no longer needed
		    carIndividualManager = new CarIndividualManager(carEntityManager.getCars(), carEntityManager.getDistances(), settings.getSelectionOperator(), settings.getCrossoverOperator());
		    carEntityManager.removeAll();

		    fireCarEvent(new CarEvent(this, carIndividualManager.getCarIndividual(), finish));
		    useGeneticOperators(settings);
		    createWorld();
		    ground = null;
		    createGround(groundPoints);

		    // Creates a new batch of cars
		    CarIndividual[] ci = carIndividualManager.getCarIndividual();
		    CarEntity[] cars = new CarEntity[carIndividualManager.getNumber()];
		    for (int i = 0; i < carIndividualManager.getNumber(); i++) {
			cars[i] = new CarEntity(world, 0.0f, startingAltitude + ABOVE_GROUND, ci[i].getCarChromosome());
		    }

		    // Assigns cars to their manager,
		    carEntityManager = new CarEntityManager(cars, finish);
		    running = true;
		}

		if (cameraState == CAMERA_FOLLOW) {
		    camera.position.set(carEntityManager.getFurthestCar().getPosition().x * BOX_TO_WORLD, (carEntityManager.getFurthestCar().getPosition().y * BOX_TO_WORLD + camera.viewportHeight) * 0.4f, 0f);
		}

		camera.update();
	    }

	    world.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
	}

	long end = System.currentTimeMillis();

	try {
	    if (end - beg < FPS) Thread.sleep(FPS - (end - beg));
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    public Vector2 screenToWorld(int x, int y) {
	Vector3 v3 = new Vector3(x, y, 0);
	camera.unproject(v3);
	return new Vector2(v3.x, v3.y);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
	gameState = STATUS_PAUSED;
    }

    @Override
    public void resume() {
	gameState = STATUS_READY;
    }

    public int getGamestate() {
	return gameState;
    }

    public void end() {
	running = false;
	carEntityManager.removeAll();
    }

    @Override
    public void dispose() {
	this.dispose();
    }

    public World getWorld() {
	return world;
    }

    public void fixCamera() {
	cameraState = CAMERA_FOLLOW;
    }

    public void unfixCamera() {
	cameraState = CAMERA_FREE;
    }

    // --------------------------------------------------
    // Helpers
    // --------------------------------------------------

    private void useGeneticOperators(AlgorithmSettings settings) {
	if (settings.isElite()) carIndividualManager.rememberBest();

	carIndividualManager.selection();
	carIndividualManager.crossover();
	carIndividualManager.mutation(settings.getMutationProbability(), specs);

	if (settings.isElite()) carIndividualManager.reviveBest();
    }

    // --------------------------------------------------
    // Settings
    // --------------------------------------------------

    public void setSettings(AlgorithmSettings settings) {
	this.settings = settings;

	world.setGravity(new Vector2(0, -settings.getGravity()));
    }

    // --------------------------------------------------
    // Listeners
    // --------------------------------------------------

    public synchronized void addCarListener(CarListener listener) {
	if (!carListenerList.contains(listener)) {
	    carListenerList.add(listener);
	}
    }

    @SuppressWarnings("unchecked")
    private void fireCarEvent(CarEvent carEvent) {
	ArrayList<CarListener> tempCarListenerList;

	synchronized (this) {
	    if (carListenerList.size() == 0)
		return;
	    tempCarListenerList = (ArrayList<CarListener>) carListenerList.clone();
	}

	for (CarListener listener : tempCarListenerList) {
	    listener.carsReceived(carEvent);
	}
    }
}