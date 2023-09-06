package poc.adapter;

import poc.dqlang.constants.Technical;
import poc.util.ConstantsLoader;

public class TechnicalAdapter {

    private final static String newLine = System.lineSeparator();

    public String adapt() {
        StringBuilder builder = new StringBuilder();
        builder.append("technical {" + newLine);

        Technical technical = ConstantsLoader.load().getTechnical();
        int warmUpDuration = technical.getWarmUpDuration();
        int coolDownDuration = technical.getCoolDownDuration();
        int thinkTime = technical.getThinkTime();

        builder.append("warmUpDuration = " + warmUpDuration + newLine);
        builder.append("coolDownDuration = " + coolDownDuration + newLine);
        builder.append("thinkTime = " + thinkTime + newLine);
        builder.append("}" + newLine);

        return builder.toString();
    }
}
