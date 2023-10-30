package racingcar.view;

import java.util.List;
import racingcar.domain.RacingGameStatus;

public class IOView implements View {
    private final InputView inputView;
    private final OutputView outputView;

    public IOView(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    @Override
    public List<String> getCarNames() {
        outputView.requestCarNames(inputView.getDelimiterDescription(), inputView.getDelimiter());
        return inputView.enterCarNames();
    }

    @Override
    public int getRacingCount() {
        outputView.requestTryCount();
        return inputView.enterTryCount();
    }

    @Override
    public void showRacingGameStatus(RacingGameStatus racingGameStatus) {
        outputView.printRacingGameStatus(racingGameStatus);
    }
}
