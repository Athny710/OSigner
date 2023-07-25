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

    public static void createInstace(SignConfiguration signConfiguration){
        instance = signConfiguration;

    }

    public static void updateInstance(List<FileModel> filesToSign){
        instance.filesToSign = filesToSign;

    }

    public static SignConfiguration getInstance(){
        return instance;

    }

    public SignProcessModel getSignProcessConfiguration() {
        return signProcessConfiguration;
    }


    public List<FileModel> getFilesToSign() {
        return filesToSign;
    }

}
