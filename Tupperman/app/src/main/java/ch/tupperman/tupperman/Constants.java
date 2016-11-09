package ch.tupperman.tupperman;

public interface Constants {

    public enum RequestCode {
        LOGIN(1), REGISTER(2);

        private final int mValue;

        RequestCode(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

}
