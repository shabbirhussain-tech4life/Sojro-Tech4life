package com.mdcbeta;

/**
 * Created by Shakil Karim on 4/18/17.
 */

public final class Events {


    public static class Start {
    }

    public static class Default{

    }

    public static class Success {
    }


    public static class Progress {

        private int progress;

        public Progress(int progress) {
            this.progress = progress;
        }

        public Progress() {
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }
    }



    public static class Failed {

        private String message;

        public Failed(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }


}
