package entity;

public class Teacher {
    private String tno;
    private String tname;
    private String tdept;
    private String tschool;

    public Teacher() {}

    public String getTno() {
        return tno;
    }

    public void setTno(String tno) {
        this.tno = tno;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTdept() {
        return tdept;
    }

    public void setTdept(String tdept) {
        this.tdept = tdept;
    }

    public String getTschool() {
        return tschool;
    }

    public void setTschool(String tschool) {
        this.tschool = tschool;
    }

    @Override
    public String toString() {
        return "Teacher{tno='" + tno + "', tname='" + tname + "', tdept='" + tdept + "'}";
    }

}
