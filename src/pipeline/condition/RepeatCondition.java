package pipeline.condition;

import java.util.HashMap;

public class RepeatCondition<I> extends PipelineCondition<I> {

    private final HashMap<I, Integer> repeatRepository = new HashMap<>();

    private final int repeat;

    public RepeatCondition(int repeat) {
        this.repeat = repeat;
    }

    @Override
    public boolean isTrue(I in) {
        if (!this.repeatRepository.containsKey(in))
            this.repeatRepository.put(in, repeat);

        int r = this.repeatRepository.get(in);

        if (r > 0) {
            this.repeatRepository.replace(in, r - 1);
            return true;
        }
        else {
            this.repeatRepository.remove(in);
            return false;
        }
    }

}
