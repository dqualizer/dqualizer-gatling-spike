package poc.adapter;

import poc.dqlang.constants.TechnicalConstants;
import poc.util.ConstantsLoader;

public class TechnicalAdapter {

    private final static String newLine = System.lineSeparator();

    public String adapt() {
        StringBuilder builder = new StringBuilder();
        builder.append("technicalConstants {" + newLine);

        TechnicalConstants technicalConstants = ConstantsLoader.load().getTechnicalConstants();
        int warmUpDuration = technicalConstants.getWarmUpDuration();
        int coolDownDuration = technicalConstants.getCoolDownDuration();
        int thinkTime = technicalConstants.getThinkTime();

        builder.append("warmUpDuration = " + warmUpDuration + newLine);
        builder.append("coolDownDuration = " + coolDownDuration + newLine);
        builder.append("thinkTime = " + thinkTime + newLine);
        builder.append("}" + newLine);

        return builder.toString();
    }
}
