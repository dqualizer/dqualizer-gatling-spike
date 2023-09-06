package poc.util;

import poc.dqlang.constants.GatlingConstants;
import poc.dqlang.constants.symbolics.generic.SymbolicDuration;
import poc.dqlang.constants.symbolics.generic.SymbolicLoad;
import poc.dqlang.loadtest.stimulus.symbolic.SymbolicDoubleValue;
import poc.dqlang.loadtest.stimulus.symbolic.SymbolicIntValue;
import poc.dqlang.loadtest.stimulus.symbolic.SymbolicValue;
import poc.exception.UnknownTypeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SymbolicTransformer {

    public final static String LOAD = "load";
    public final static String DURATION = "duration";

    public static Number calculateValue(SymbolicValue symbolicValue) {
        if(symbolicValue instanceof SymbolicIntValue symbolicIntValue) {
            return calculateIntValue(symbolicIntValue);
         }
        else if(symbolicValue instanceof SymbolicDoubleValue symbolicDoubleValue) {
            return calculateDoubleValue(symbolicDoubleValue);
        }
        else throw new UnknownTypeException(symbolicValue.getClass().getName());
    }

    public static Number calculateTimeUnit(Number number, String type) {
        GatlingConstants constants = ConstantsLoader.load();

        switch (type) {
            case LOAD -> {
                TimeUnit timeUnit = constants.getSymbolics().getInteger().
            }
            case DURATION -> {

            }
            default -> throw new UnknownTypeException(type);
        }
    }

    private static Integer calculateIntValue(SymbolicIntValue symbolicIntValue) {
        GatlingConstants constants = ConstantsLoader.load();
        String name = symbolicIntValue.getName();

        SymbolicLoad<Integer> loadConstants = constants.getSymbolics().getInteger().getLoad();
        SymbolicDuration<Integer> durationConstants = constants.getSymbolics().getInteger().getDuration();

        switch (name) {
            case "LOW" -> {
                return loadConstants.getLow();
            }
            case "MEDIUM" -> {
                return loadConstants.getMedium();
            }
            case "HIGH" -> {
                return loadConstants.getHigh();
            }
            case "SLOW" -> {
                return durationConstants.getSlow();
            }
            case "FAST" -> {
                return durationConstants.getFast();
            }
            case "VERY_FAST" -> {
                return durationConstants.getVeryFast();
            }
            default -> throw new UnknownTypeException(name);
        }
    }

    private static Double calculateDoubleValue(SymbolicDoubleValue symbolicDoubleValue) {
        GatlingConstants constants = ConstantsLoader.load();
        String name = symbolicDoubleValue.getName();

        SymbolicLoad<Double> loadConstants = constants.getSymbolics().getDecimal().getLoad();
        SymbolicDuration<Double> durationConstants = constants.getSymbolics().getDecimal().getDuration();

        switch (name) {
            case "LOW" -> {
                return loadConstants.getLow();
            }
            case "MEDIUM" -> {
                return loadConstants.getMedium();
            }
            case "HIGH" -> {
                return loadConstants.getHigh();
            }
            case "SLOW" -> {
                return durationConstants.getSlow();
            }
            case "FAST" -> {
                return durationConstants.getFast();
            }
            case "VERY_FAST" -> {
                return durationConstants.getVeryFast();
            }
            default -> throw new UnknownTypeException(name);
        }
    }
}
