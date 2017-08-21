package com.yunding.dut.model.resp.reading;

import java.io.Serializable;
import java.util.List;

/**
 * 类 名 称：markResp
 * <P/>描    述：标记返回
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:16
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:16
 * <P/>修改备注：
 * <P/>版    本：
 */

public class markResp {

    /**
     * data : {"toDeleteNewWordIds":["4332"],"savedWord":{"newWordId":"4333","word":"Economic","wordColor":2,"wordIndex":813,"wordLength":8}}
     * errors : {}
     * msg : ok
     * result : true
     * version : 1.0
     */

    private DataBean data;
    private ErrorsBean errors;
    private String msg;
    private boolean result;
    private String version;


    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(ErrorsBean errors) {
        this.errors = errors;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static class DataBean {

        public DataBean.notesBean getNotesBean() {
            return notes;
        }

        public void setNotesBean(DataBean.notesBean notesBean) {
            this.notes = notesBean;
        }

        /**
         * toDeleteNewWordIds : ["4332"]
         * savedWord : {"newWordId":"4333","word":"Economic","wordColor":2,"wordIndex":813,"wordLength":8}
         */
        private notesBean notes;
        public static class notesBean implements Serializable{
            public String getNoteContent() {
                return noteContent;
            }

            public void setNoteContent(String noteContent) {
                this.noteContent = noteContent;
            }

            public String getNoteTime() {
                return noteTime;
            }

            public void setNoteTime(String noteTime) {
                this.noteTime = noteTime;
            }

            private String noteContent;
            private String  noteTime;

        }
        public int getNoted() {
            return noted;
        }


        public void setNoted(int noted) {
            this.noted = noted;
        }

        private int noted;

        private SavedWordBean savedWord;
        private List<String> toDeleteNewWordIds;

        public SavedWordBean getSavedWord() {
            return savedWord;
        }

        public void setSavedWord(SavedWordBean savedWord) {
            this.savedWord = savedWord;
        }

        public List<String> getToDeleteNewWordIds() {
            return toDeleteNewWordIds;
        }

        public void setToDeleteNewWordIds(List<String> toDeleteNewWordIds) {
            this.toDeleteNewWordIds = toDeleteNewWordIds;
        }

        public static class SavedWordBean implements Serializable{
            /**
             * newWordId : 4333
             * word : Economic
             * wordColor : 2
             * wordIndex : 813
             * wordLength : 8
             */

            private String newWordId;
            private String word;
            private int wordColor;






            public int getWordLength() {
                return wordLength;
            }

            public void setWordLength(int wordLength) {
                this.wordLength = wordLength;
            }

            private int wordLength;
            private int wordIndex;

            public String getNewWordId() {
                return newWordId;
            }

            public void setNewWordId(String newWordId) {
                this.newWordId = newWordId;
            }

            public String getWord() {
                return word;
            }

            public void setWord(String word) {
                this.word = word;
            }

            public int getWordColor() {
                return wordColor;
            }

            public void setWordColor(int wordColor) {
                this.wordColor = wordColor;
            }

            public int getWordIndex() {
                return wordIndex;
            }

            public void setWordIndex(int wordIndex) {
                this.wordIndex = wordIndex;
            }
        }
    }

    public static class ErrorsBean {
    }
}
