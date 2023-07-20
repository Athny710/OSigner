package org.oefa.gob.pe.osigner.domain;

import java.util.List;

public class SignConfiguration {

    private static SignConfiguration instance;


    private SignProcessModel signProcessConfiguration;
    private List<FileModel> filesToSign;

    public SignConfiguration(SignProcessModel signProcessModel, List<FileModel> filesToSign){
        this.signProcessConfiguration = signProcessModel;
        this.filesToSign = filesToSign;
    }
    private SignConfiguration(SignConfiguration signConfiguration){

    }
    public static SignConfiguration createInstace(SignProcessModel signProcessModel, List<FileModel> filesToSign){
        instance = new SignConfiguration(signProcessModel, filesToSign);

        return instance;
    }
    public static SignConfiguration getInstance(){
        return instance;

    }

    public SignProcessModel getSignProcessConfiguration() {
        return signProcessConfiguration;
    }

    public void setSignProcessConfiguration(SignProcessModel signProcessConfiguration) {
        this.signProcessConfiguration = signProcessConfiguration;
    }

    public List<FileModel> getFilesToSign() {
        return filesToSign;
    }

    public void setFilesToSign(List<FileModel> filesToSign) {
        this.filesToSign = filesToSign;
    }
}
