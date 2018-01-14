package com.sheygam.java_17_14_01_18;

/**
 * Created by gregorysheygam on 14/01/2018.
 */

public class Manager {
    private String name;
    private String email;
    private int salary;

    private Manager() {
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getSalary() {
        return salary;
    }

    public static class Builder{
        private String bName;
        private String bemail;
        private int bSalary;

        public Builder name(String name){
            bName = name;
            return this;
        }

        public Builder email(String email){
            bemail = email;
            return this;
        }

        public Builder salary(int salary){
            bSalary = salary;
            return this;
        }

        public Manager build(){
            Manager m = new Manager();
            m.name = bName;
            m.email = bemail;
            m.salary = (int) (bSalary*0.85);
            return m;
        }
    }
}
