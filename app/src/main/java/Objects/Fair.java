package Objects;

public class Fair {
    private String id;
    private String[] companies;
    private String desc;
    private String name;
    private String university;

    public Fair(String id, String[] companies, String desc, String name, String university) {
        this.id = id;
        this.companies = companies;
        this.desc = desc;
        this.name = name;
        this.university = university;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getCompanies() {
        return companies;
    }

    public void setCompanies(String[] companies) {
        this.companies = companies;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }
}
