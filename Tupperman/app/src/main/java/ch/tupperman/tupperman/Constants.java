package ch.tupperman.tupperman;

interface Constants {

    enum RequestCode {
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
