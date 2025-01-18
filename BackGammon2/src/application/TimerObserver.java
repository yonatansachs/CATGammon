package application;

import javafx.scene.control.Label;

class TimerObserver implements EventListener {
    private Label timerLabel;

    public TimerObserver(Label timerLabel) {
        this.timerLabel = timerLabel;
    }

    @Override
    public void update(String eventType, Object data) {
        if ("timerUpdated".equals(eventType) && data instanceof String) {
            timerLabel.setText((String) data);
        }
    }
}
