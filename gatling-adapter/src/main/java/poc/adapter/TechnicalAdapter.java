package poc.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.dqlang.constants.TechnicalConstants;
import poc.util.ConstantsLoader;

@Component
public class TechnicalAdapter {

    @Autowired
    private ConstantsLoader constantsLoader;

    private final static String newLine = System.lineSeparator();

    public String adapt() {
        StringBuilder builder = new StringBuilder();
        builder.append("technicalConstants {" + newLine);

        TechnicalConstants technicalConstants = constantsLoader.load().getTechnicalConstants();
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
