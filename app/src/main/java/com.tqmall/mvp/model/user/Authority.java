package com.tqmall.mvp.model.user;

import java.io.Serializable;
import java.util.ArrayList;


public class Authority implements Serializable {
    public String groupName;
    public String comments;
    public ArrayList<Authority> menuVoList;
}
