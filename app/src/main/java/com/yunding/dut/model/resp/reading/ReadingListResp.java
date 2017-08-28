package com.yunding.dut.model.resp.reading;

import java.io.Serializable;
import java.util.List;

/**
 * 类 名 称：ReadingListResp
 * <P/>描    述：阅读列表返回结果
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/24 16:25
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/24 16:25
 * <P/>修改备注：
 * <P/>版    本：
 */

public class ReadingListResp implements Serializable {

    /**
     * data : [{"articleFinish":1,"completed":1,"courseContent":"The rush to the cities was one of the global trends of the twentieth century. In the beginning of the century, only 14 percent of the world's population lived in cities. By the end, the majority of the population lived in cities, and these cities were overcrowded and polluted. The twenty-first century faces a population crisis, and specialists from a variety of fields from engineering to agriculture are meeting to create more sustainable living environments outside of the major urban areas. Gaviotas, a rural area in Colombia, is an example of how even desolate places in the countryside can be developed for sustainable human use. \n \nThis area of Colombia had been considered a wasteland: dry brown savanna, without enough water, plant, or animal life to support a town. But today as one approaches the town, a green spot appears on the brown horizon, and big aluminum sunflowers begin to dot the empty savanna. As it turns out, the green spot is a 25,OOO-acre forest and the sunflowers are windmills.\nUnder the trees are low white buildings and colorful houses, all with solar collectors. Begun in 1971 as a scientific experiment, Gaviotas is now a self-sufficient town of 200, supported by clean, renewable industries. A handful of Colombian engineers and soil chemists were the first to settle in Gaviotas. They were persuaded by a Colombian visionary5 named Paolo Lugari to try to make an unlivable place livable. Lugari predicted that by the twenty-first century, expanding populations would have to live in \"unlivable\" places. He wanted to see if it would be possible to restore the unproductive savanna to a place where humans could live well.\n\nThe first problem was finding pure water. The Gaviotans invented a special kind of hand pump to reach deepwater reservoirs. They developed solar heaters to use the sun's energy to clean the drinking water and soil-free hydroponic6 systems to raise food. These inventions have spread to other parts of Latin America; nearly 700 villages in Colombia alone now use the pumps developed in Gaviotas.\n\nAnother goal was to restore the soi17 and plants. After years of experimentation, Gaviotas scientists discovered that Caribbean pines from Honduras could grow in the area's thin, acidic soil. At the same time, resin from the trees could be tapped without cutting down the forest, and used in paints, cosmetics, perfumes and medicines.\n\nBesides providing a sustainable living, the pines have also created what biologists call a miracle: After several years of healthy growth, a 25,OOO-acre tropical forest developed on the once dry savanna. Soon animal life such as deer and hawks were seen. The 250 plant species identified so far have been studied in the Gaviotan's research lab for possible medicinal uses.\n\nAs more people moved to Gaviotas, it was important to consider a pollution-free way of transportation. Village mechanics developed a bicycle especially designed for the area's rough land. Bicycles are now the official mode of transportation, cheap and pollution-free.\nGaviotas is a remarkable accomplishment of sustainable development. This once dry landscape has been turned into a near paradise.\n","courseId":"14","courseTitle":"ruxuan The rush to the cities was one of the global trends of the twentieth century.","createTime":"2017-05-05 15:27:23","exercises":[{"analysis":"proaches the town","answerContent":"A","answerDate":"2017-05-05 15:31:15","options":[{"optionContent":"proaches the town","optionId":"84","optionIndex":"A"},{"optionContent":"proaches the town","optionId":"85","optionIndex":"B"},{"optionContent":"proaches the town","optionId":"86","optionIndex":"C"},{"optionContent":"proaches the town","optionId":"87","optionIndex":"D"}],"position":2,"questionCompleted":1,"questionContent":"dry brown savanna, without enough water, plant, or animal life to support a town. But today as one approaches the town, a green spot appears on the brown horizon","questionId":"41","questionIndex":1,"questionType":2,"rightAnswer":"A"}],"newWords":[{"newWordId":"142","word":"overcrowded","wordIndex":253,"wordLength":11},{"newWordId":"144","word":"century","wordIndex":296,"wordLength":7},{"newWordId":"131","word":"create","wordIndex":419,"wordLength":6}],"paragraphQuantity":6,"preClassExercises":[{"analysis":"This area of Colombia had been considered a wasteland","answerContent":"[\"of\",\"ofColombiahadbeen\",\"beenconsideredawasteland\",\"ofColombiahadbeenconsideredawasteland\"]","answerDate":"2017-05-05 15:30:48","options":[],"position":1,"questionCompleted":1,"questionContent":"This area of Colombia had been considered a wasteland: dry brown savanna, without enough water, plant, or animal life to support a town. But today as one approaches the town, a green spot appears on the brown horizon, and big aluminum sunflowers begin to dot the empty savanna. As it turns out, the green spot is a 25,OOO-acre forest and the sunflowers are windmills.","questionId":"40","questionIndex":1,"questionType":1,"rightAnswer":"[\"This area of Colombia had been considered a wasteland\",\"This area of Colombia had been considered a wasteland\",\"This area of Colombia had been considered a wasteland\",\"This area of Colombia had been considered a wasteland\"]"}],"readingLineIndex":29,"sentenceInfo":[{"index":0,"length":77},{"index":77,"length":92},{"index":169,"length":109},{"index":278,"length":218},{"index":496,"length":141},{"index":637,"length":193},{"index":830,"length":141},{"index":971,"length":90},{"index":1061,"length":88},{"index":1149,"length":131},{"index":1280,"length":89},{"index":1369,"length":108},{"index":1477,"length":115},{"index":1592,"length":118},{"index":1710,"length":43},{"index":1753,"length":82},{"index":1835,"length":130},{"index":1965,"length":141},{"index":2106,"length":51},{"index":2157,"length":142},{"index":2299,"length":143},{"index":2442,"length":205},{"index":2647,"length":51},{"index":2698,"length":118},{"index":2816,"length":104},{"index":2920,"length":85},{"index":3005,"length":80},{"index":3085,"length":68},{"index":3153,"length":62},{"index":3215,"length":1}],"sentenceQuantity":29,"standardReadingTime":1000,"studentId":"44","wordQuantity":510}]
     * errors : {}
     * msg : ok
     * result : true
     * version : 20170422,lin
     */

    private ErrorsBean errors;
    private String msg;
    private boolean result;
    private String version;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class ErrorsBean implements Serializable {
    }

    public static class DataBean implements Serializable {
        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        /**
         * articleFinish : 1
         * completed : 1
         * courseContent : The rush to the cities was one of the global trends of the twentieth century. In the beginning of the century, only 14 percent of the world's population lived in cities. By the end, the majority of the population lived in cities, and these cities were overcrowded and polluted. The twenty-first century faces a population crisis, and specialists from a variety of fields from engineering to agriculture are meeting to create more sustainable living environments outside of the major urban areas. Gaviotas, a rural area in Colombia, is an example of how even desolate places in the countryside can be developed for sustainable human use.
         * <p>
         * This area of Colombia had been considered a wasteland: dry brown savanna, without enough water, plant, or animal life to support a town. But today as one approaches the town, a green spot appears on the brown horizon, and big aluminum sunflowers begin to dot the empty savanna. As it turns out, the green spot is a 25,OOO-acre forest and the sunflowers are windmills.
         * Under the trees are low white buildings and colorful houses, all with solar collectors. Begun in 1971 as a scientific experiment, Gaviotas is now a self-sufficient town of 200, supported by clean, renewable industries. A handful of Colombian engineers and soil chemists were the first to settle in Gaviotas. They were persuaded by a Colombian visionary5 named Paolo Lugari to try to make an unlivable place livable. Lugari predicted that by the twenty-first century, expanding populations would have to live in "unlivable" places. He wanted to see if it would be possible to restore the unproductive savanna to a place where humans could live well.
         * <p>
         * The first problem was finding pure water. The Gaviotans invented a special kind of hand pump to reach deepwater reservoirs. They developed solar heaters to use the sun's energy to clean the drinking water and soil-free hydroponic6 systems to raise food. These inventions have spread to other parts of Latin America; nearly 700 villages in Colombia alone now use the pumps developed in Gaviotas.
         * <p>
         * Another goal was to restore the soi17 and plants. After years of experimentation, Gaviotas scientists discovered that Caribbean pines from Honduras could grow in the area's thin, acidic soil. At the same time, resin from the trees could be tapped without cutting down the forest, and used in paints, cosmetics, perfumes and medicines.
         * <p>
         * Besides providing a sustainable living, the pines have also created what biologists call a miracle: After several years of healthy growth, a 25,OOO-acre tropical forest developed on the once dry savanna. Soon animal life such as deer and hawks were seen. The 250 plant species identified so far have been studied in the Gaviotan's research lab for possible medicinal uses.
         * <p>
         * As more people moved to Gaviotas, it was important to consider a pollution-free way of transportation. Village mechanics developed a bicycle especially designed for the area's rough land. Bicycles are now the official mode of transportation, cheap and pollution-free.
         * Gaviotas is a remarkable accomplishment of sustainable development. This once dry landscape has been turned into a near paradise.
         * <p>
         * courseId : 14
         * courseTitle : ruxuan The rush to the cities was one of the global trends of the twentieth century.
         * createTime : 2017-05-05 15:27:23
         * exercises : [{"analysis":"proaches the town","answerContent":"A","answerDate":"2017-05-05 15:31:15","options":[{"optionContent":"proaches the town","optionId":"84","optionIndex":"A"},{"optionContent":"proaches the town","optionId":"85","optionIndex":"B"},{"optionContent":"proaches the town","optionId":"86","optionIndex":"C"},{"optionContent":"proaches the town","optionId":"87","optionIndex":"D"}],"position":2,"questionCompleted":1,"questionContent":"dry brown savanna, without enough water, plant, or animal life to support a town. But today as one approaches the town, a green spot appears on the brown horizon","questionId":"41","questionIndex":1,"questionType":2,"rightAnswer":"A"}]
         * newWords : [{"newWordId":"142","word":"overcrowded","wordIndex":253,"wordLength":11},{"newWordId":"144","word":"century","wordIndex":296,"wordLength":7},{"newWordId":"131","word":"create","wordIndex":419,"wordLength":6}]
         * paragraphQuantity : 6
         * preClassExercises : [{"analysis":"This area of Colombia had been considered a wasteland","answerContent":"[\"of\",\"ofColombiahadbeen\",\"beenconsideredawasteland\",\"ofColombiahadbeenconsideredawasteland\"]","answerDate":"2017-05-05 15:30:48","options":[],"position":1,"questionCompleted":1,"questionContent":"This area of Colombia had been considered a wasteland: dry brown savanna, without enough water, plant, or animal life to support a town. But today as one approaches the town, a green spot appears on the brown horizon, and big aluminum sunflowers begin to dot the empty savanna. As it turns out, the green spot is a 25,OOO-acre forest and the sunflowers are windmills.","questionId":"40","questionIndex":1,"questionType":1,"rightAnswer":"[\"This area of Colombia had been considered a wasteland\",\"This area of Colombia had been considered a wasteland\",\"This area of Colombia had been considered a wasteland\",\"This area of Colombia had been considered a wasteland\"]"}]
         * readingLineIndex : 29
         * sentenceInfo : [{"index":0,"length":77},{"index":77,"length":92},{"index":169,"length":109},{"index":278,"length":218},{"index":496,"length":141},{"index":637,"length":193},{"index":830,"length":141},{"index":971,"length":90},{"index":1061,"length":88},{"index":1149,"length":131},{"index":1280,"length":89},{"index":1369,"length":108},{"index":1477,"length":115},{"index":1592,"length":118},{"index":1710,"length":43},{"index":1753,"length":82},{"index":1835,"length":130},{"index":1965,"length":141},{"index":2106,"length":51},{"index":2157,"length":142},{"index":2299,"length":143},{"index":2442,"length":205},{"index":2647,"length":51},{"index":2698,"length":118},{"index":2816,"length":104},{"index":2920,"length":85},{"index":3005,"length":80},{"index":3085,"length":68},{"index":3153,"length":62},{"index":3215,"length":1}]
         * sentenceQuantity : 29
         * standardReadingTime : 1000.0
         * studentId : 44
         * wordQuantity : 510
         */
        private String classId;
        /**
         * 阅读模式 1分句 2分段 3全篇
         */

        private int readingMode;

        /**
         * 阅读截止时间
         */
        private String deadline;

        /**
         * 是否显示关键句 0不显示 1显示
         */
        private String showKeyWord;

        /**
         * 填空题分值
         */
        private String blankScore;

        /**
         * 单选题分值
         */
        private String selectScore;

        /**
         * 多选题分值
         */
        private String multiSelectScore;

        /**
         * 翻译题分值
         */
        private String tranScore;

        /**
         * 答案解析显示时机 0答完及时显示 1阅读课结束显示
         */
        private String answerShowTime;

        /**
         * 总分值
         */
        private String totalScore;

        /**
         * 单元名称
         */
        private String unitName;

        /**
         * 单元题目
         */
        private String unitTitle;

        /**
         * 单元ID
         */
        private String unitId;

        public int getReadingMode() {
            return readingMode;
        }

        public void setReadingMode(int readingMode) {
            this.readingMode = readingMode;
        }

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        public String getShowKeyWord() {
            return showKeyWord;
        }

        public void setShowKeyWord(String showKeyWord) {
            this.showKeyWord = showKeyWord;
        }

        public String getBlankScore() {
            return blankScore;
        }

        public void setBlankScore(String blankScore) {
            this.blankScore = blankScore;
        }

        public String getSelectScore() {
            return selectScore;
        }

        public void setSelectScore(String selectScore) {
            this.selectScore = selectScore;
        }

        public String getMultiSelectScore() {
            return multiSelectScore;
        }

        public void setMultiSelectScore(String multiSelectScore) {
            this.multiSelectScore = multiSelectScore;
        }

        public String getTranScore() {
            return tranScore;
        }

        public void setTranScore(String tranScore) {
            this.tranScore = tranScore;
        }

        public String getAnswerShowTime() {
            return answerShowTime;
        }

        public void setAnswerShowTime(String answerShowTime) {
            this.answerShowTime = answerShowTime;
        }

        public String getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(String totalScore) {
            this.totalScore = totalScore;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getUnitTitle() {
            return unitTitle;
        }

        public void setUnitTitle(String unitTitle) {
            this.unitTitle = unitTitle;
        }

        public String getUnitId() {
            return unitId;
        }

        public void setUnitId(String unitId) {
            this.unitId = unitId;
        }

        private int articleFinish;
        private int completed;
        private String courseContent;
        private String courseId;
        private String courseTitle;
        private String createTime;
        private int paragraphQuantity;
        private int readingLineIndex;
        private int sentenceQuantity;
        private double standardReadingTime;
        private String studentId;
        private int wordQuantity;
        private List<ExercisesBean> exercises;
        private List<NewWordsBean> newWords;
        private List<PreClassExercisesBean> preClassExercises;
        private List<SentenceInfoBean> sentenceInfo;
        private List<EducationKeyword> educationkeywords;//关键主旨句


        public List<EducationKeyword> getEducationkeywords() {
            return educationkeywords;
        }

        public void setEducationkeywords(List<EducationKeyword> educationkeywords) {
            this.educationkeywords = educationkeywords;
        }

        public static class EducationKeyword implements Serializable {
            /****/
            private String keywordId;

            /**
             * 关键句或主旨句
             **/
            private String keyWord;

            /**
             * 归属阅读课id
             **/
            private String courseId;

            /**
             * 标记时间
             **/
            private String markDate;

            /**
             * 文章中的下标
             **/
            private Integer wordIndex;

            /**
             * 单词词长
             **/
            private Integer wordLength;

            /**
             * 标记用户ID
             **/
            private String markUserId;

            /**
             * 0主旨句1关键句
             **/
            private Integer wordType;

            public void setKeywordId(String KeywordId) {
                this.keywordId = KeywordId;
            }

            public String getKeywordId() {
                return this.keywordId;
            }

            public void setKeyWord(String KeyWord) {
                this.keyWord = KeyWord;
            }

            public String getKeyWord() {
                return this.keyWord;
            }

            public void setCourseId(String CourseId) {
                this.courseId = CourseId;
            }

            public String getCourseId() {
                return this.courseId;
            }

            public void setMarkDate(String MarkDate) {
                this.markDate = MarkDate;
            }

            public String getMarkDate() {
                return this.markDate;
            }

            public void setWordIndex(Integer WordIndex) {
                this.wordIndex = WordIndex;
            }

            public Integer getWordIndex() {
                return this.wordIndex;
            }

            public void setWordLength(Integer WordLength) {
                this.wordLength = WordLength;
            }

            public Integer getWordLength() {
                return this.wordLength;
            }

            public void setMarkUserId(String MarkUserId) {
                this.markUserId = MarkUserId;
            }

            public String getMarkUserId() {
                return this.markUserId;
            }

            public void setWordType(Integer WordType) {
                this.wordType = WordType;
            }

            public Integer getWordType() {
                return this.wordType;
            }

        }

        public int getArticleFinish() {
            return articleFinish;
        }

        public void setArticleFinish(int articleFinish) {
            this.articleFinish = articleFinish;
        }

        public int getCompleted() {
            return completed;
        }

        public void setCompleted(int completed) {
            this.completed = completed;
        }

        public String getCourseContent() {
            return courseContent;
        }

        public void setCourseContent(String courseContent) {
            this.courseContent = courseContent;
        }

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }

        public String getCourseTitle() {
            return courseTitle;
        }

        public void setCourseTitle(String courseTitle) {
            this.courseTitle = courseTitle;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getParagraphQuantity() {
            return paragraphQuantity;
        }

        public void setParagraphQuantity(int paragraphQuantity) {
            this.paragraphQuantity = paragraphQuantity;
        }

        public int getReadingLineIndex() {
            return readingLineIndex;
        }

        public void setReadingLineIndex(int readingLineIndex) {
            this.readingLineIndex = readingLineIndex;
        }

        public int getSentenceQuantity() {
            return sentenceQuantity;
        }

        public void setSentenceQuantity(int sentenceQuantity) {
            this.sentenceQuantity = sentenceQuantity;
        }

        public double getStandardReadingTime() {
            return standardReadingTime;
        }

        public void setStandardReadingTime(double standardReadingTime) {
            this.standardReadingTime = standardReadingTime;
        }

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }

        public int getWordQuantity() {
            return wordQuantity;
        }

        public void setWordQuantity(int wordQuantity) {
            this.wordQuantity = wordQuantity;
        }

        public List<ExercisesBean> getExercises() {
            return exercises;
        }

        public void setExercises(List<ExercisesBean> exercises) {
            this.exercises = exercises;
        }

        public List<NewWordsBean> getNewWords() {
            return newWords;
        }

        public void setNewWords(List<NewWordsBean> newWords) {
            this.newWords = newWords;
        }

        public List<PreClassExercisesBean> getPreClassExercises() {
            return preClassExercises;
        }

        public void setPreClassExercises(List<PreClassExercisesBean> preClassExercises) {
            this.preClassExercises = preClassExercises;
        }

        public List<SentenceInfoBean> getSentenceInfo() {
            return sentenceInfo;
        }

        public void setSentenceInfo(List<SentenceInfoBean> sentenceInfo) {
            this.sentenceInfo = sentenceInfo;
        }

        public static class ExercisesBean implements Serializable {
            public List<InputBlank> getBlanksInfo() {
                return blanksInfo;
            }

            public void setBlanksInfo(List<InputBlank> blanksInfo) {
                this.blanksInfo = blanksInfo;
            }

            /**
             * analysis : proaches the town
             * answerContent : A 用户答案
             * answerDate : 2017-05-05 15:31:15
             * options : [{"optionContent":"proaches the town","optionId":"84","optionIndex":"A"},{"optionContent":"proaches the town","optionId":"85","optionIndex":"B"},{"optionContent":"proaches the town","optionId":"86","optionIndex":"C"},{"optionContent":"proaches the town","optionId":"87","optionIndex":"D"}]
             * position : 2
             * questionCompleted : 1
             * questionContent : dry brown savanna, without enough water, plant, or animal life to support a town. But today as one approaches the town, a green spot appears on the brown horizon
             * questionId : 41
             * questionIndex : 1
             * questionType : 2
             * rightAnswer : A 正确答案
             */
           private List<InputBlank> blanksInfo;
            private String analysis;
            private String answerContent;
            private String answerDate;
            private int position;
            private int questionCompleted;
            private String questionContent;
            private String questionId;
            private int questionIndex;
            private int questionType;
            private String rightAnswer;
            private List<OptionsBean> options;
            private List<EducationAnswerFrom> educationAnswerFroms;//答案出处
            public static class InputBlank implements Serializable{
                public int getIndex() {
                    return index;
                }

                public void setIndex(int index) {
                    this.index = index;
                }

                public int getLength() {
                    return length;
                }

                public void setLength(int length) {
                    this.length = length;
                }

                private int index;
                private int length;
            }


            public List<EducationAnswerFrom> getEducationAnswerFroms() {
                return educationAnswerFroms;
            }

            public void setEducationAnswerFroms(List<EducationAnswerFrom> educationAnswerFroms) {
                this.educationAnswerFroms = educationAnswerFroms;
            }

            public static class EducationAnswerFrom implements Serializable {
                /****/
                private String formId;

                /**
                 * 文章中的下标
                 **/
                private Integer wordIndex;

                /**
                 * 单词词长
                 **/
                private Integer wordLength;

                /**
                 * 问题ID
                 **/
                private String questionId;

                /**
                 * 内容
                 **/
                private String content;

                /**
                 * 课程ID
                 **/
                private String courseId;


                public void setFormId(String FormId) {
                    this.formId = FormId;
                }

                public String getFormId() {
                    return this.formId;
                }

                public void setWordIndex(Integer WordIndex) {
                    this.wordIndex = WordIndex;
                }

                public Integer getWordIndex() {
                    return this.wordIndex;
                }

                public void setWordLength(Integer WordLength) {
                    this.wordLength = WordLength;
                }

                public Integer getWordLength() {
                    return this.wordLength;
                }

                public void setQuestionId(String QuestionId) {
                    this.questionId = QuestionId;
                }

                public String getQuestionId() {
                    return this.questionId;
                }

                public void setContent(String Content) {
                    this.content = Content;
                }

                public String getContent() {
                    return this.content;
                }

                public void setCourseId(String CourseId) {
                    this.courseId = CourseId;
                }

                public String getCourseId() {
                    return this.courseId;
                }
            }

            public String getAnalysis() {
                return analysis;
            }

            public void setAnalysis(String analysis) {
                this.analysis = analysis;
            }

            public String getAnswerContent() {
                return answerContent;
            }

            public void setAnswerContent(String answerContent) {
                this.answerContent = answerContent;
            }

            public String getAnswerDate() {
                return answerDate;
            }

            public void setAnswerDate(String answerDate) {
                this.answerDate = answerDate;
            }

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public int getQuestionCompleted() {
                return questionCompleted;
            }

            public void setQuestionCompleted(int questionCompleted) {
                this.questionCompleted = questionCompleted;
            }

            public String getQuestionContent() {
                return questionContent;
            }

            public void setQuestionContent(String questionContent) {
                this.questionContent = questionContent;
            }

            public String getQuestionId() {
                return questionId;
            }

            public void setQuestionId(String questionId) {
                this.questionId = questionId;
            }

            public int getQuestionIndex() {
                return questionIndex;
            }

            public void setQuestionIndex(int questionIndex) {
                this.questionIndex = questionIndex;
            }

            public int getQuestionType() {
                return questionType;
            }

            public void setQuestionType(int questionType) {
                this.questionType = questionType;
            }

            public String getRightAnswer() {
                return rightAnswer;
            }

            public void setRightAnswer(String rightAnswer) {
                this.rightAnswer = rightAnswer;
            }

            public List<OptionsBean> getOptions() {
                return options;
            }

            public void setOptions(List<OptionsBean> options) {
                this.options = options;
            }

            public static class OptionsBean implements Serializable {
                /**
                 * optionContent : proaches the town
                 * optionId : 84
                 * optionIndex : A
                 */

                private String optionContent;
                private String optionId;
                private String optionIndex;

                public String getOptionContent() {
                    return optionContent;
                }

                public void setOptionContent(String optionContent) {
                    this.optionContent = optionContent;
                }

                public String getOptionId() {
                    return optionId;
                }

                public void setOptionId(String optionId) {
                    this.optionId = optionId;
                }

                public String getOptionIndex() {
                    return optionIndex;
                }

                public void setOptionIndex(String optionIndex) {
                    this.optionIndex = optionIndex;
                }
            }
        }

        public static class NewWordsBean implements Serializable {
            /**
             * newWordId : 142
             * word : overcrowded
             * wordIndex : 253
             * wordLength : 11
             */
            private String collected;//是否收藏 0没收藏 1收藏过

            public String getCollected() {
                return collected;
            }

            public void setCollected(String collected) {
                this.collected = collected;
            }

            public int getNoted() {
                return noted;
            }

            public void setNoted(int noted) {
                this.noted = noted;
            }


            private int noted;//是否有笔记0 没有笔记 1有笔记

            public static class notesBean implements Serializable {
                public String getNoteTime() {
                    return noteTime;
                }

                public void setNoteTime(String noteTime) {
                    this.noteTime = noteTime;
                }

                private String noteTime;

                public String getNoteContent() {
                    return noteContent;
                }

                public void setNoteContent(String noteContent) {
                    this.noteContent = noteContent;
                }

                private String noteContent;
            }


            public notesBean getNotes() {
                return notes;
            }

            public void setNotes(notesBean notes) {
                this.notes = notes;
            }

            private notesBean notes;
            private String newWordId;
            private String word;
            private int wordIndex;
            private int wordLength;

            public int getWordColor() {
                return wordColor;
            }

            public void setWordColor(int wordColor) {
                this.wordColor = wordColor;
            }

            private int wordColor;//颜色


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

            public int getWordIndex() {
                return wordIndex;
            }

            public void setWordIndex(int wordIndex) {
                this.wordIndex = wordIndex;
            }

            public int getWordLength() {
                return wordLength;
            }

            public void setWordLength(int wordLength) {
                this.wordLength = wordLength;
            }
        }


        public static class PreClassExercisesBean implements Serializable {
            public List<InputBlank> getBlanksInfo() {
                return blanksInfo;
            }

            public void setBlanksInfo(List<InputBlank> blanksInfo) {
                this.blanksInfo = blanksInfo;
            }

            /**
             * analysis : This area of Colombia had been considered a wasteland
             * answerContent : ["of","ofColombiahadbeen","beenconsideredawasteland","ofColombiahadbeenconsideredawasteland"]
             * answerDate : 2017-05-05 15:30:48
             * options : []
             * position : 1
             * questionCompleted : 1
             * questionContent : This area of Colombia had been considered a wasteland: dry brown savanna, without enough water, plant, or animal life to support a town. But today as one approaches the town, a green spot appears on the brown horizon, and big aluminum sunflowers begin to dot the empty savanna. As it turns out, the green spot is a 25,OOO-acre forest and the sunflowers are windmills.
             * questionId : 40
             * questionIndex : 1
             * questionType : 1
             * rightAnswer : ["This area of Colombia had been considered a wasteland","This area of Colombia had been considered a wasteland","This area of Colombia had been considered a wasteland","This area of Colombia had been considered a wasteland"]
             */

            private List<InputBlank>blanksInfo;
            private String analysis;
            private String answerContent;
            private String answerDate;
            private int position;
            private int questionCompleted;
            private String questionContent;
            private String questionId;
            private int questionIndex;
            private int questionType;
            private String rightAnswer;
            private List<OptionsBeanX> options;
            public static class InputBlank implements Serializable{
                private int index;
                private int length;

                public int getIndex() {
                    return index;
                }

                public void setIndex(int index) {
                    this.index = index;
                }

                public int getLength() {
                    return length;
                }

                public void setLength(int length) {
                    this.length = length;
                }
            }

            public String getAnalysis() {
                return analysis;
            }

            public void setAnalysis(String analysis) {
                this.analysis = analysis;
            }

            public String getAnswerContent() {
                return answerContent;
            }

            public void setAnswerContent(String answerContent) {
                this.answerContent = answerContent;
            }

            public String getAnswerDate() {
                return answerDate;
            }

            public void setAnswerDate(String answerDate) {
                this.answerDate = answerDate;
            }

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public int getQuestionCompleted() {
                return questionCompleted;
            }

            public void setQuestionCompleted(int questionCompleted) {
                this.questionCompleted = questionCompleted;
            }

            public String getQuestionContent() {
                return questionContent;
            }

            public void setQuestionContent(String questionContent) {
                this.questionContent = questionContent;
            }

            public String getQuestionId() {
                return questionId;
            }

            public void setQuestionId(String questionId) {
                this.questionId = questionId;
            }

            public int getQuestionIndex() {
                return questionIndex;
            }

            public void setQuestionIndex(int questionIndex) {
                this.questionIndex = questionIndex;
            }

            public int getQuestionType() {
                return questionType;
            }

            public void setQuestionType(int questionType) {
                this.questionType = questionType;
            }

            public String getRightAnswer() {
                return rightAnswer;
            }

            public void setRightAnswer(String rightAnswer) {
                this.rightAnswer = rightAnswer;
            }

            public List<OptionsBeanX> getOptions() {
                return options;
            }

            public void setOptions(List<OptionsBeanX> options) {
                this.options = options;
            }

            public static class OptionsBeanX implements Serializable {
                /**
                 * optionContent : Rupert’s teacher.
                 * optionId : 149
                 * optionIndex : A
                 */

                private String optionContent;
                private String optionId;
                private String optionIndex;

                public String getOptionContent() {
                    return optionContent;
                }

                public void setOptionContent(String optionContent) {
                    this.optionContent = optionContent;
                }

                public String getOptionId() {
                    return optionId;
                }

                public void setOptionId(String optionId) {
                    this.optionId = optionId;
                }

                public String getOptionIndex() {
                    return optionIndex;
                }

                public void setOptionIndex(String optionIndex) {
                    this.optionIndex = optionIndex;
                }
            }
        }

        public static class SentenceInfoBean implements Serializable {
            /**
             * index : 0
             * length : 77
             */

            private int index;
            private int length;

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public int getLength() {
                return length;
            }

            public void setLength(int length) {
                this.length = length;
            }
        }
    }
}
