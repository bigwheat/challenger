package com.tqmall.global;

import java.lang.reflect.Field;

/**
 * Created by Jay on 16/8/15.
 */

public class BaseBean {

    @Override
    public String toString() {
            StringBuffer sb = new StringBuffer();
            try {
                Class t = this.getClass();
                Field[] fields = t.getDeclaredFields();
                sb.append("{");
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    field.setAccessible(true);
                    sb.append("\"");
                    sb.append( field.getName());
                    sb.append("\":");
                    if (field.getType() == Integer.class) {
                        sb.append("\""+field.get(this));
                    } else if (field.getType() == Long.class) {
                        sb.append("\""+field.get(this));
                    } else if (field.getType() == boolean.class) {
                        sb.append("\""+field.get(this));
                    } else if (field.getType() == char.class) {
                        sb.append("\""+field.get(this));
                    } else if (field.getType() == Double.class) {
                        sb.append("\""+field.get(this));
                    } else if (field.getType() == Float.class) {
                        sb.append("\""+field.get(this));
                    } else if (field.getType() == String.class) {
                        sb.append("\""+field.get(this));
                    }
                    else{

                        Object tmp=field.get(this);
                        if(tmp==null )
                            sb.append("\""+field.get(this));
                        else
                            sb.append(field.get(this));
                    }

                    if(sb.indexOf("]")==(sb.length()-1))
                        sb.append("");
                    else
                        sb.append("\"");

                    if(i !=(fields.length-1))
                        sb.append(",");
                }
                sb.append("}");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
    }

