package poc.util;

import poc.dqlang.constants.GatlingConstants;
import poc.dqlang.constants.symbolics.generic.SymbolicDurationType;
import poc.dqlang.constants.symbolics.generic.SymbolicLoadType;
import poc.dqlang.loadtest.stimulus.symbolic.SymbolicDoubleValue;
import poc.dqlang.loadtest.stimulus.symbolic.SymbolicIntValue;
import poc.dqlang.loadtest.stimulus.symbolic.SymbolicValue;
import poc.exception.UnknownTypeException;

import java.util.concurrent.TimeUnit;

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

    public static Number calculateTimeUnit(Number value, String type) {
        GatlingConstants constants = ConstantsLoader.load();
        long longValue = value.longValue();

        switch (type) {
            case LOAD -> {
                TimeUnit timeUnit = constants.getSymbolics().getLoad().getTimeUnit();
                // Since load uses the timeUnit in the denominator (for example user/SECONDS),
                // the value has to be divided and not multiplied
                // Since toSeconds() always uses multiplication, the factor is extracted and after that used for division
                double newValue = timeUnit.toSeconds(longValue);
                double multiplicationFactor = newValue / longValue;
                return longValue / multiplicationFactor;
            }
            case DURATION -> {
                TimeUnit timeUnit = constants.getSymbolics().getDuration().getTimeUnit();
                return timeUnit.toSeconds(longValue);
            }
            default -> throw new UnknownTypeException(type);
        }
    }

    private static Integer calculateIntValue(SymbolicIntValue symbolicIntValue) {
        GatlingConstants constants = ConstantsLoader.load();
        String name = symbolicIntValue.getName();

        SymbolicLoadType<Integer> loadConstants = constants.getSymbolics().getLoad().getInteger();
        SymbolicDurationType<Integer> durationConstants = constants.getSymbolics().getDuration().getInteger();

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

        SymbolicLoadType<Double> loadConstants = constants.getSymbolics().getLoad().getDecimal();
        SymbolicDurationType<Double> durationConstants = constants.getSymbolics().getDuration().getDecimal();

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
