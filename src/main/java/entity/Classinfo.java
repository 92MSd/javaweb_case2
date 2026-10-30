package entity;

public class Classinfo {
    private String classname;
    private String specno;

    public Classinfo() {
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSpecno() {
        return specno;
    }

    public void setSpecno(String specno) {
        this.specno = specno;
    }

    @Override
    public String toString() {
        return "Classinfo{classname='" + classname + "', specno='" + specno + "'}";
    }
}
