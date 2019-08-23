package com.scrm.why1139.BioReferenceModule;

import java.util.List;

public class BioCertificater
{
    public static List<String> certificateUser(String _strBase64)
    {
        return AIBound.FaceQuery(_strBase64);
    }
}
