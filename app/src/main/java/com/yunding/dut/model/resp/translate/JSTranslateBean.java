package com.yunding.dut.model.resp.translate;

import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */

public class JSTranslateBean {


    /**
     * word_name : persuaded
     * is_CRI : 0
     * exchange : {"word_pl":"","word_third":"","word_past":"","word_done":"","word_ing":"","word_er":"","word_est":""}
     * symbols : [{"ph_en":"","ph_am":"","ph_other":"http://res-tts.iciba.compəˈsweidid","ph_en_mp3":"","ph_am_mp3":"","ph_tts_mp3":"http://res-tts.iciba.com/7/b/b/7bb9d03e32449f08c37f990ea6f0dfaa.mp3","parts":[{"part":"v.","means":["说服( persuade的过去式和过去分词 )","劝告","使信服","使相信"]}]}]
     */

    private String word_name;
    private String is_CRI;
    private List<SymbolsBean> symbols;

    public String getWord_name() {
        return word_name;
    }

    public void setWord_name(String word_name) {
        this.word_name = word_name;
    }

    public String getIs_CRI() {
        return is_CRI;
    }

    public void setIs_CRI(String is_CRI) {
        this.is_CRI = is_CRI;
    }


    public List<SymbolsBean> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<SymbolsBean> symbols) {
        this.symbols = symbols;
    }



    public static class SymbolsBean {
        /**
         * ph_en :
         * ph_am :
         * ph_other : http://res-tts.iciba.compəˈsweidid
         * ph_en_mp3 :
         * ph_am_mp3 :
         * ph_tts_mp3 : http://res-tts.iciba.com/7/b/b/7bb9d03e32449f08c37f990ea6f0dfaa.mp3
         * parts : [{"part":"v.","means":["说服( persuade的过去式和过去分词 )","劝告","使信服","使相信"]}]
         */

        private String ph_en;
        private String ph_am;
        private String ph_other;
        private String ph_en_mp3;
        private String ph_am_mp3;
        private String ph_tts_mp3;
        private List<PartsBean> parts;

        public String getPh_en() {
            return ph_en;
        }

        public void setPh_en(String ph_en) {
            this.ph_en = ph_en;
        }

        public String getPh_am() {
            return ph_am;
        }

        public void setPh_am(String ph_am) {
            this.ph_am = ph_am;
        }

        public String getPh_other() {
            return ph_other;
        }

        public void setPh_other(String ph_other) {
            this.ph_other = ph_other;
        }

        public String getPh_en_mp3() {
            return ph_en_mp3;
        }

        public void setPh_en_mp3(String ph_en_mp3) {
            this.ph_en_mp3 = ph_en_mp3;
        }

        public String getPh_am_mp3() {
            return ph_am_mp3;
        }

        public void setPh_am_mp3(String ph_am_mp3) {
            this.ph_am_mp3 = ph_am_mp3;
        }

        public String getPh_tts_mp3() {
            return ph_tts_mp3;
        }

        public void setPh_tts_mp3(String ph_tts_mp3) {
            this.ph_tts_mp3 = ph_tts_mp3;
        }

        public List<PartsBean> getParts() {
            return parts;
        }

        public void setParts(List<PartsBean> parts) {
            this.parts = parts;
        }

        public static class PartsBean {
            /**
             * part : v.
             * means : ["说服( persuade的过去式和过去分词 )","劝告","使信服","使相信"]
             */

            private String part;
            private List<String> means;

            public String getPart() {
                return part;
            }

            public void setPart(String part) {
                this.part = part;
            }

            public List<String> getMeans() {
                return means;
            }

            public void setMeans(List<String> means) {
                this.means = means;
            }
        }
    }
}
