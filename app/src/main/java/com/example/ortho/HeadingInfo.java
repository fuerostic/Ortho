package com.example.ortho;

public class HeadingInfo {

    private String heading,value;

    public HeadingInfo(String heading, String value) {
        this.heading = heading;
        this.value = value;
    }

    public HeadingInfo() {
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
