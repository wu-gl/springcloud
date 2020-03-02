package com.example.ribbonuser;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description UserConfig
 * @Author 吴桂林
 * @Date 2020/1/15 10:01
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "student")
public class UserConfig {
    private String name;
    private  int age;
    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "StudentConfig{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                '}';
    }
}
