package Objects;

public class Company {
    private String[] employees;
    private String id;
    private String[] roles;
    private String bio;
    private String website;
    private String name;

    public Company(String[] employees, String id, String[] roles, String bio, String website, String name) {
        this.employees = employees;
        this.id = id;
        this.roles = roles;
        this.bio = bio;
        this.website = website;
        this.name = name;
    }

    public String[] getEmployees() {
        return employees;
    }

    public void setEmployees(String[] employees) {
        this.employees = employees;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
