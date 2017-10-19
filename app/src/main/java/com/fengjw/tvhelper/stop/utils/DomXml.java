package com.fengjw.tvhelper.stop.utils;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by fengjw on 2017/10/19.
 */

public class DomXml {
    private Context mContext;
    private List<Filter> mList;
    public DomXml(Context context){
        mContext = context;
        mList = new ArrayList<Filter>();
    }

    public List<Filter> XMLResolve(){
        StringWriter xmlWriter = new StringWriter();
        try {
            InputStream is = mContext.getAssets().open("filter.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is); //这里获得了xml并开始解析
            doc.getDocumentElement().normalize();
            NodeList nlRoot = doc.getElementsByTagName("root");
            Element eleRoot = (Element) nlRoot.item(0);
            String attrAuthor = eleRoot.getAttribute("author");
            String attrDate = eleRoot.getAttribute("date");
            xmlWriter.append(attrAuthor).append("\t\t\t");
            xmlWriter.append(attrDate).append("\n");

            NodeList nlFilter = eleRoot.getElementsByTagName("filter");
            int filerLength = nlFilter.getLength();
            Filter [] filters = new Filter[filerLength];
            for (int i = 0; i < filerLength; i ++){
                Element element = (Element) nlFilter.item(i);
                Filter filter = new Filter();
                NodeList nlName = element.getElementsByTagName("name");
                Element element1 = (Element) nlName.item(0);
                String name = element1.getChildNodes().item(0).getNodeValue();
                filter.setName(name);
                xmlWriter.append(filter.toString() + "\n");
                filters[i] = filter;
                mList.add(filter);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        //return xmlWriter.toString();
        return mList;
    }

}
