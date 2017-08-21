package com.yunding.dut.model.resp.ppt;

import java.io.Serializable;
import java.util.List;

/**
 * 类 名 称：PPTResp
 * <P/>描    述：ppt信息返回值
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:15
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:15
 * <P/>修改备注：
 * <P/>版    本：
 */

public class PPTResp {

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

    private ErrorsBean errors;
    private String msg;
    private boolean result;
    private String version;
    private List<DataBean> data;


    public static class ErrorsBean implements Serializable {
    }

    public static class DataBean implements Serializable {
//                "answerTimeLimit": 3, // integer 答题限时，0表示不限时
//                "sendAnswerAnalysis": 1,// integer 推送答案解析，0不推送1推送
//                "sendDate": "2017-07-15 10:56:54",// string 推送时间
//                "sendQuestionFlag": 1,// integer 推送小题，0不推送1推送
//                "showEndDate": "2017-07-15 10:57:03",// string 展示结束时间
//                "showStartDate": "2017-07-15 10:56:48",// string 展示开始时间

        public String getPlatformTime() {
            return platformTime;
        }

        public void setPlatformTime(String platformTime) {
            this.platformTime = platformTime;
        }

        //          "slideId": 256,// 幻灯片ID
//                  "slideImage": "upload/pptimgs/2017/07/15/1500087418806.jpg",// string 幻灯片图片地址（相对路径）
//                  "slideQuestions": [// 存放幻灯片小题信息列表，按照questionIndex升序排列
        private  String platformTime;
        private int answerTimeLimit;

        public int getQuestionsCompleted() {
            return questionsCompleted;
        }

        public void setQuestionsCompleted(int questionsCompleted) {
            this.questionsCompleted = questionsCompleted;
        }

        private int questionsCompleted; // 当前幻灯片的问题是否全部答完 0-未完成 1-完成，如果小题未推送，设置为1（完成）

        public String getTeachingId() {
            return teachingId;
        }

        public void setTeachingId(String teachingId) {
            this.teachingId = teachingId;
        }

        private String teachingId;
        public Long getStartTime() {
            return startTime;
        }

        public void setStartTime(Long startTime) {
            this.startTime = startTime;
        }

        public String getPptStartTime() {
            return pptStartTime;
        }

        public void setPptStartTime(String pptStartTime) {
            this.pptStartTime = pptStartTime;
        }

        public List<String> getPptImageList() {
            return pptImageList;
        }

        public void setPptImageList(List<String> pptImageList) {
            this.pptImageList = pptImageList;
        }

        public List<String>pptImageList;

        private String pptStartTime;
        private Long startTime;
        private int sendAnswerAnalysis;
        private String sendDate;
        private String showEndDate;
        private String showStartDate;
        private List<slideFilesBean> slideFiles;//附件信息列表
        private String slideId;
        private String slideImage;
        private List<slideQuestionsBean> slideQuestions;
        private String teachingSlideId;

        public String getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(String pageIndex) {
            this.pageIndex = pageIndex;
        }

        private String pageIndex;

        public String getSpecialityId() {
            return specialityId;
        }

        public void setSpecialityId(String specialityId) {
            this.specialityId = specialityId;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        private String specialityId;
        private String classId;

        public int getAnswerTimeLimit() {
            return answerTimeLimit;
        }

        public void setAnswerTimeLimit(int answerTimeLimit) {
            this.answerTimeLimit = answerTimeLimit;
        }

        public int getSendAnswerAnalysis() {
            return sendAnswerAnalysis;
        }

        public void setSendAnswerAnalysis(int sendAnswerAnalysis) {
            this.sendAnswerAnalysis = sendAnswerAnalysis;
        }

        public String getSendDate() {
            return sendDate;
        }

        public void setSendDate(String sendDate) {
            this.sendDate = sendDate;
        }

        public String getShowEndDate() {
            return showEndDate;
        }

        public void setShowEndDate(String showEndDate) {
            this.showEndDate = showEndDate;
        }

        public String getShowStartDate() {
            return showStartDate;
        }

        public void setShowStartDate(String showStartDate) {
            this.showStartDate = showStartDate;
        }

        public List<slideFilesBean> getSlideFiles() {
            return slideFiles;
        }

        public void setSlideFiles(List<slideFilesBean> slideFiles) {
            this.slideFiles = slideFiles;
        }

        public String getSlideId() {
            return slideId;
        }

        public void setSlideId(String slideId) {
            this.slideId = slideId;
        }

        public String getSlideImage() {
            return slideImage;
        }

        public void setSlideImage(String slideImage) {
            this.slideImage = slideImage;
        }

        public List<slideQuestionsBean> getSlideQuestions() {
            return slideQuestions;
        }

        public void setSlideQuestions(List<slideQuestionsBean> slideQuestions) {
            this.slideQuestions = slideQuestions;
        }

        public String getTeachingSlideId() {
            return teachingSlideId;
        }

        public void setTeachingSlideId(String teachingSlideId) {
            this.teachingSlideId = teachingSlideId;
        }

        public static class slideFilesBean implements Serializable {
            //             "fileId": // integer 附件唯一标识
//                     "fileType": // integer 附件类型 1-视频 2-音频 3-动图
//                     "fileUrl":// string 附件地址（相对路径）
//                     "createDate":// string 创建时间
            private String fileId;
            private int fileType;
            private String fileUrl;
            private String createDate;

            public String getFileId() {
                return fileId;
            }

            public void setFileId(String fileId) {
                this.fileId = fileId;
            }

            public int getFileType() {
                return fileType;
            }

            public void setFileType(int fileType) {
                this.fileType = fileType;
            }

            public String getFileUrl() {
                return fileUrl;
            }

            public void setFileUrl(String fileUrl) {
                this.fileUrl = fileUrl;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }
        }

        public static class slideQuestionsBean implements Serializable {

            //            "questionId":// integer 小题ID
//                    "questionContent"// string 小题内容
//                    "questionType":// integer 小题类型 1-填空 2-单选 3-多选 4-问答 5-枚举
//                    "rightAnswer":// string 填空类型题的正确答案为json字符串，多选类型题答案用英文逗号分隔的字符串
//                    "analysis":// string 答案解析
//                    "questionIndex":// integer 小题顺序号
//                    "sendAnalysisFlag":// integer 0-手动发布答案解析 1答完发布答案解析
//                    "questionCompleted":// integer 小题是否完成 0-未完成 1-完成
//                    "answerContent":// string 学生答案 形式如rightAnswer
//                     optionsList
            public  List<InputBlank> getBlanksInfo() {
                return blanksInfo;
            }public void             setBlanksInfo(List<InputBlank> blanksInfo) {
                this.blanksInfo = blanksInfo;
            }

            public int getAnswerTimeLimit() {
                return answerTimeLimit;
            }

            public void setAnswerTimeLimit(int answerTimeLimit) {
                this.answerTimeLimit = answerTimeLimit;
            }

            public int getSubmitType() {
                return submitType;
            }

            public void setSubmitType(int submitType) {
                this.submitType = submitType;
            }

            private  int submitType;
            private  int answerTimeLimit;
            private List<InputBlank>blanksInfo;
            private String questionId;
            private String questionContent;
            private int questionType;
            private String rightAnswer;
            private String analysis;
            private String questionIndex;
            private int sendAnalysisFlag;
            private int questionCompleted;
            private String answerContent;
            private List<optionsListBean> optionsList;
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

            public String getQuestionId() {
                return questionId;
            }

            public void setQuestionId(String questionId) {
                this.questionId = questionId;
            }

            public String getQuestionContent() {
                return questionContent;
            }

            public void setQuestionContent(String questionContent) {
                this.questionContent = questionContent;
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

            public String getAnalysis() {
                return analysis;
            }

            public void setAnalysis(String analysis) {
                this.analysis = analysis;
            }

            public String getQuestionIndex() {
                return questionIndex;
            }

            public void setQuestionIndex(String questionIndex) {
                this.questionIndex = questionIndex;
            }

            public int getSendAnalysisFlag() {
                return sendAnalysisFlag;
            }

            public void setSendAnalysisFlag(int sendAnalysisFlag) {
                this.sendAnalysisFlag = sendAnalysisFlag;
            }

            public int getQuestionCompleted() {
                return questionCompleted;
            }

            public void setQuestionCompleted(int questionCompleted) {
                this.questionCompleted = questionCompleted;
            }

            public String getAnswerContent() {
                return answerContent;
            }

            public void setAnswerContent(String answerContent) {
                this.answerContent = answerContent;
            }

            public List<optionsListBean> getOptionsList() {
                return optionsList;
            }

            public void setOptionsList(List<optionsListBean> optionsList) {
                this.optionsList = optionsList;
            }

            public static class optionsListBean implements Serializable {
                //                 "optionId":// integer 选项ID
//                         "optionContent":// string 选项内容
//                         "isRight":// integer 是否是正确答案 0-不正确 1-正确
//                         "optionIndex"// string 选项序号 如A,B,C等
                public String getOptionId() {
                    return optionId;
                }

                public void setOptionId(String optionId) {
                    this.optionId = optionId;
                }

                public String getIsRight() {
                    return isRight;
                }

                public void setIsRight(String isRight) {
                    this.isRight = isRight;
                }

                public String getOptionContent() {
                    return optionContent;
                }

                public void setOptionContent(String optionContent) {
                    this.optionContent = optionContent;
                }

                public String getOptionIndex() {
                    return optionIndex;
                }

                public void setOptionIndex(String optionIndex) {
                    this.optionIndex = optionIndex;
                }

                private String optionId;
                private String isRight;
                private String optionContent;
                private String optionIndex;

            }


        }

    }
}
