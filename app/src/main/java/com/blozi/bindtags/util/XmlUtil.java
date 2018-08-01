package com.blozi.bindtags.util;


import android.text.TextUtils;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import java.beans.Encoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static com.blozi.bindtags.util.SystemConstants.TEMP_DIR;

/**
 * @aothor 骆长涛
 * @datetime 2018-02-10 a:58
 * @description 控制层的工具
 **/
public class XmlUtil {
    /**
     @Description: 将参数转化为xml 文档以便发送
      * @author 骆长涛
     * @date 2018-03-08 下午18:32:19
     * @param parameterMap 参数
     * @param action action
     * @param identifier 登陆用户的标识码
     * @param rootStr 根元素的字符，比如请求用request ，响应用response
     * @return Document
     *
     */
    public static Document paramterToXml(Map parameterMap,String action,String identifier , String rootStr) throws JSONException{
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement(rootStr);
        Map<String ,String> map0 = new HashMap<String, String>();
        map0.put("action",action);
        map0.put("identifier",identifier);
        mapToElement(root,map0,false);
        Element parameters = root.addElement("parameterMap");
        mapToElement(parameters,parameterMap,true);
//        System.out.println(document.asXML());
        return document;
    }

    public static Document requsetMapToXml(Map parameterMap,String action,String identifier ) throws JSONException{
        return paramterToXml(parameterMap,action,identifier,"request");
    }
    /**
     @Description: 将参数转化为xml 文档以便发送，所有参数都已经整理好了。
      * @author 骆长涛
     * @date 2018-03-08 下午18:32:19
     * @param map 参数
     * @param rootElementName 根元素的字符，比如请求用request ，响应用response
     * @return Document
     *
     */
    public static Document mapToXml(Map map, String rootElementName) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement(rootElementName);
        mapToElement(root,map,false);
        return document;
    }
    public static Document responseMapToXml(Map map) throws JSONException{
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("response");
        mapToElement(root,map,false);
        return document;
    }

    public static Document paramterToXml(Map parameterMap,Map otherMainElement, String rootElementName) throws JSONException{
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement(rootElementName);
        mapToElement(root,otherMainElement,false);
        Element parameters = root.addElement("parameterMap");
            mapToElement(parameters,parameterMap,true);
        return  document;
    }
    public static Document requestMapToXml(Map parameterMap,Map otherMainElement) throws JSONException{
        return  paramterToXml(parameterMap,otherMainElement,"request");
    }
    /**
     * @description 由于spring控制层的参数Map form 被锁死。所以如果有额外的参数需要提交时使用这个方法
     * */
    public static Document paramterToXml(Map map,String action,String identifier,Map other, String rootElementName)throws JSONException{
        Set set = map.keySet();
        for(Object o:set){
            other.put(o,map.get(o));
        }
        return paramterToXml(other,action,identifier,rootElementName);
    }
    public static Document requestMapToXml(Map map,String action,String identifier,Map other)throws JSONException{
        return paramterToXml(map, action,identifier,other,"request");
    }
    /**
     @Description: 将xml 转化为map数据\n <p>注意一定要处理特殊字符。推荐使用本类中map转为xml文件，DOM4J会自动处理特殊字符</p>
      * @author 骆长涛
     * @date 2018-02-10 下午15:32:19
     * @param xmlStr xml字符串
     * @return Document
     *
     */
    public static Map<String,Object> xmlToMap(String  xmlStr)throws JSONException{
        Map map= new HashMap();
        try {
            Document document = DocumentHelper.parseText(xmlStr);
            Element root =document.getRootElement();
            List<Element> list= root.elements();
            for(Element ele:list){
                List<Element> childrens= ele.elements();
                String pname = ele.getName();
                Attribute name = ele.attribute("name");
                if(name!=null && !TextUtils.isEmpty(name.getValue()))pname =name.getValue();

                Attribute type = ele.attribute("type");
                String value = ele.getText();
                if(childrens==null || childrens.size()==0){
                    if(  ParameterChecker.checkStringParameter(pname)   ){
                        if(  map.get(pname) !=null   ){
                            if( map.get(pname) instanceof String  ){
                                String  parms =  map.get(pname).toString();
                                String [] parms_new =new String[2];
                                parms_new[0] = parms;
                                parms_new[1] = value;
                                map.put(pname,parms_new);
//                            logger.info("数组参数："+parms+"\t"+value);
                            }else if( map.get(pname) instanceof String[]  ){
                                String [] parms = (String []) map.get(pname);
                                String [] parms_new =new String[parms.length+1];
                                for(int i=0;i<parms.length;i++){
//								logger.info("数组参数"+(i+1)+"："+parms+"\t"+value);
                                    parms_new[i]=parms[i];
                                }
                                parms_new[parms.length] = value;
                                map.put(pname,parms_new);
                            }else if( map.get(pname) instanceof File){
                                File [] files = new File[2];
                                files[0] = (File)map.get(pname);
                                Attribute fileName = ele.attribute("fileName");
                                byte[] f = FileUtil.decoderBase64File(value);
                                String dirName = TEMP_DIR+"/"+ UUID.randomUUID().toString();
                                if(f!=null){
                                    try {
                                        files[1] = FileUtil.saveFile(f,fileName.getValue(),dirName);
                                        map.put(pname,files);
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }
                                }
                            }else if( map.get(pname) instanceof File[]  ){

                                File[] files = (File[])map.get(pname);
                                File [] nfiles = new File[files.length+1];
                                for(int i=0;i<files.length;i++){
//								logger.info("数组参数"+(i+1)+"："+parms+"\t"+value);
                                    nfiles[i]=files[i];
                                }
                                Attribute fileName = ele.attribute("fileName");
                                byte[] f = FileUtil.decoderBase64File(value);
                                String dirName = TEMP_DIR+"/"+ UUID.randomUUID().toString();
                                if(f!=null){
                                    try {
                                        nfiles[files.length] = FileUtil.saveFile(f,fileName.getValue(),dirName);
                                        map.put(pname,nfiles);
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }else{
                            if(type!=null &&  "File".equals( type.getValue())){
                                try {
                                    Attribute fileName = ele.attribute("fileName");
                                    byte[] f = FileUtil.decoderBase64File(value);
                                    String dirName = TEMP_DIR+"/"+ UUID.randomUUID().toString();
                                    if(f!=null){
                                        File file = FileUtil.saveFile(f,fileName.getValue(),dirName);
                                        map.put(pname,file);
                                    }
                                }catch (IOException e){
                                    e.printStackTrace();
                                }
                            }else{
                                map.put(pname,value);
                            }
                        }
                    }
                }else if(childrens.size()>0){
                    if("JSONObject".equals(childrens.get(0).getName())){
                        JSONArray jsonArray = new JSONArray();
                        for(Element element:childrens){
                            List<Element> arrts = element.elements();
                            JSONObject jsonObject = new JSONObject();
                            for(Element arrt:arrts){
                                jsonObject.put(arrt.getName(),arrt.getText());
                            }
                            jsonArray.put(jsonObject);
                        }
                        map.put(ele.getName(),jsonArray);
                    }else{
                        JSONObject jsonObject = new JSONObject();
                        for(Element element:childrens){
                            jsonObject.put(element.getName(),element.getText());
                        }
                        map.put(ele.getName(),jsonObject);
                    }
                }
            }
            return map;
        }catch (DocumentException e){
            e.printStackTrace();
            map.put("isSuccess","n");
            map.put("msg","不能正确获取数据");
            return map;
        }

    }

    private static void addElementsByJSONObject(Element root,JSONObject jsonObject){
        Iterator<String> sIterator = jsonObject.keys();
        while(sIterator.hasNext()){
            String key = sIterator.next();
            try {
                Object value = jsonObject.get(key);
                Element ele =root.addElement(key);
                if(value!=null && value instanceof String )ele.setText((String) value);
                else ele.setText(value.toString());
            }catch (JSONException e){
                e.printStackTrace();
            }finally {
                continue;
            }
        }
    }
    private static Element mapToElement(Element root ,Map parameterMap ,boolean isParameter ){
        Set<String> keys = parameterMap.keySet();
        String parameterStr = "parameter";
        for(String str:keys){
            if(!isParameter)parameterStr=str;
            if(parameterMap.get(str)!=null &&  ParameterChecker.checkStringParameter(parameterMap.get(str).toString())){
                Object ps = parameterMap.get(str);
//                System.out.println(str+"\t"+ ps.getClass());
                if(ps instanceof String[]){
                    String[] pars = (String[])ps;
                    if(pars!=null && pars.length>0){
                        for(String s:pars){
                            if(ParameterChecker.checkStringParameter(s)){
                                Element parameter =root.addElement(parameterStr);
                                parameter.addAttribute("name",str);
                                parameter.setText(s);
                            }
                        }
                    }
                }else if(ps instanceof String){
                    String par = (String)ps;
                    if(ParameterChecker.checkStringParameter(par)){
                        Element parameter =root.addElement(parameterStr);
                        parameter.addAttribute("name",str);
                        parameter.setText(par);
                    }
                }else if(ps instanceof File){
                    try {
                        String f=FileUtil.encodeBase64File(new FileInputStream((File) ps));
                        Element parameter =root.addElement(parameterStr);
                        parameter.addAttribute("name",str);
                        parameter.addAttribute("type","File");
                        parameter.addAttribute("fileName",((File) ps).getName());
                        parameter.setText(f);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }else if(ps instanceof File[]){
                    try {
                        File[] oldFiles = (File[]) ps;
                        for(File file:oldFiles ){
                            String f=FileUtil.encodeBase64File(new FileInputStream(file));
                            Element parameter =root.addElement(parameterStr);
                            parameter.addAttribute("name",str);
                            parameter.addAttribute("type","File");
                            parameter.addAttribute("fileName",file.getName());
                            parameter.setText(f);
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }else if(ps instanceof JSONObject){
                    JSONObject jsonObject = (JSONObject)ps ;
                    Element parameter =root.addElement(parameterStr);
                    parameter.addAttribute("name",str);
                    parameter.addAttribute("type","JSONObject");
                    addElementsByJSONObject(parameter,jsonObject);
                }else if(ps instanceof JSONArray){
                    JSONArray jsonArray = (JSONArray)ps;
                    Element parameter =root.addElement(parameterStr);
                    parameter.addAttribute("name",str);
                    parameter.addAttribute("type","JSONArray");
                    for(int i =0;i<jsonArray.length();i++){
                        try {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            Element row =parameter.addElement("JSONObject");
                            addElementsByJSONObject(row,jsonObject);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    if(ps!=null && ParameterChecker.checkStringParameter(ps.toString())){
                        Element parameter =root.addElement(parameterStr);
                        parameter.addAttribute("name",str);
                        parameter.setText(ParameterChecker.getString(parameterMap.get(str)));
                    }
                }

            }
        }
        return root;
    }
}
