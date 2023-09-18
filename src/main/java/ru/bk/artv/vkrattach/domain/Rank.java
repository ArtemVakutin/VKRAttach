package ru.bk.artv.vkrattach.domain;

public enum Rank {
    NONE("отсутствует"),
    SOLDIER("рядовой"),
    JUNIOR_SERGEANT("младший сержант"),
    SERGEANT("сержант"),
    SENIOR_SERGEANT("старший сержант"),
    JUNIOR_ENSIGN("младший прапорщик"),
    ENSIGN("прапорщик"),
    SENIOR_ENSIGN("старший прапорщик"),
    JUNIOR_LIEUTENANT("младший лейтенант"),
    LIEUTENANT("лейтенант"),
    SENIOR_LIEUTENANT("старший лейтенант"),
    CAPTAIN("капитан"),
    MAJOR("майор"),
    LIEUTENANT_COLONEL("подполковник"),
    COLONEL("полковник"),
    MAJOR_GENERAL("генерал-майор"),
    LIEUTENANT_GENERAL("генерал-лейтенант");

    final String rank;

    Rank(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }

    public enum RankType {
        NONE("отсутствует"),
        POLICE("полиция"),
        JUSTICE("юстиция"),
        INTERNAL_SERVICE("внутренняя служба");

        final String rankType;

        RankType(String rankType) {
            this.rankType = rankType;
        }

        public String getRankType() {
            return rankType;
        }
    }
}
