package ru.bk.artv.vkrattach.domain;

public enum Department {
    UPV("Уголовного права"),
    UPR ("Уголовного процесса"),
    CRIMINOLOGY ("Криминологии"),
    CRIMINALISTICS("Криминалистики"),
    ADMINISTRATIVE_LAW("Административного права и административной деятельности органов внутренних дел"),
    TGP ("Теории государства и права"),
    ORD ("Оперативно-розыскной деятельности");

    String departmentName;

    Department(String departmentName){
        this.departmentName = departmentName;
    }

    public String getDepartmentName() {
        return departmentName;
    }
}
