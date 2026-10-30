package entity;

public class Major {
    private String specno;
    private String specname;
    private String createyear;

    public Major() {}

    public String getSpecno() { return specno; }
    public void setSpecno(String specno) { this.specno = specno; }
    public String getSpecname() { return specname; }
    public void setSpecname(String specname) { this.specname = specname; }
    public String getCreateyear() { return createyear; }
    public void setCreateyear(String createyear) { this.createyear = createyear; }

    @Override
    public String toString() {
        return "Major{specno='" + specno + "', specname='" + specname + "', createyear='" + createyear + "'}";
    }
}
