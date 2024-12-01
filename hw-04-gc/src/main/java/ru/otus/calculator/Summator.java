package ru.otus.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Getter;

@Getter
public class Summator {
    private int sum;
    private int prevValue;
    private int prevPrevValue;
    private int sumLastThreeValues;
    private int someValue;
    // !!! эта коллекция должна остаться. Заменять ее на счетчик нельзя.
    private final List<Data> listValues = new ArrayList<>();
    private final Random random = new Random(10);

    // !!! сигнатуру метода менять нельзя
    public void calc(Data data) {
        listValues.add(data);
        if (listValues.size() % 100_000 == 0) {
            listValues.clear();
        }

        sum += data.getValue() + random.nextInt();

        sumLastThreeValues = data.getValue() + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = data.getValue();

        for (var idx = 0; idx < 3; idx++) {
            someValue += (sumLastThreeValues * sumLastThreeValues / (data.getValue() + 1) - sum);
            someValue = Math.abs(someValue) + listValues.size();
        }
    }
}
