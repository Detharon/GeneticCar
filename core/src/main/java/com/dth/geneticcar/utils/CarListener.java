package com.dth.geneticcar.utils;

import java.util.EventListener;

public interface CarListener extends EventListener {
    public void carsReceived(CarEvent e);
}
