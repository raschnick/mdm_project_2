package ch.zhaw.deeplearningjava.mdm_project_2.model;

public class MaliciousUrlForm {
    private String url;
    private Double maliciousProbability;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getMaliciousProbability() {
        return maliciousProbability;
    }

    public void setMaliciousProbability(Double maliciousProbability) {
        this.maliciousProbability = maliciousProbability;
    }
}
