# Genetic Car

This is an archive of a very old project, made around 2012 in Java 1.7. It was created for my undergraduate degree
that presented a concept of generating simple 2d models that adapt themselves to the track they have to pass using
a genetic algorithm.

I decided to publish it more than a decade later, to keep it alive and maintained.

![Showcase](https://github.com/Detharon/GeneticCar/blob/master/docs/recording-2025-11-22.gif)

## How does it work

Each simulation round evaluates a population of cars. The initial population either is completely randomized, or can be
created from the previously saved state. As the simulation progresses, cars that travel farther receive a higher 
probability of being selected for the next generation, where they produce offspring through crossover and mutation.

The simulation is highly customizable. You can adjust, for example:
- Track layout and environmental gravity
- Car properties such as elasticity, wheel friction, and density (weight)
- Structural limits, including the number of wheels and car body polygons
- Genetic algorithm properties, such as the selection method, crossover strategy, and mutation probability

## Supported languages

Currently, the whole UI and internal docs are in Polish only. Sorry!
I plan to translate it to English in the future, but due to how old this project is and how much other work it needs,
it might take a while.

## Project structure

Genetic Car is based on LibGDX and follows a basic structure of a multi-platform LibGDX project.

All logic resides in the **code** project, while the **desktop** one provides just a single window to launch it.
The **desktop** project is responsible for creating the executable. The executable is a fat jar, but it might be still
buggy a bit. If you want to run it, then I recommend using a custom `run` task in Gradle.

Originally, this project did not use Gradle, but Ant, and I haven't ported it properly yet.

## Known issues

- Encoding issues in various places
- The build to distribute it (`dist`) doesn't create a proper folder structure that can hold custom data such as tracks
- 