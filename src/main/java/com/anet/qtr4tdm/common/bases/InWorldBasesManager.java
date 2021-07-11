package com.anet.qtr4tdm.common.bases;

import java.util.ArrayList;

public class InWorldBasesManager {
    ArrayList<baseInfo> bases;
    public static InWorldBasesManager instance;

    public InWorldBasesManager() {
        instance = this;
    }
}
