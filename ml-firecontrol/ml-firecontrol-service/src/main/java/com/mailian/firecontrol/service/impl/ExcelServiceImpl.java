package com.mailian.firecontrol.service.impl;

import com.mailian.firecontrol.common.enums.ItemStype;
import com.mailian.firecontrol.dto.push.DeviceItem;
import com.mailian.firecontrol.service.ExcelService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public Map<String, List<List<String>>> getExportItem(List<DeviceItem> items){
        Map<String, List<List<String>>> res = new HashMap<>();
        res.put("ycs", new ArrayList<>());
        res.put("yxs", new ArrayList<>());
        res.put("yts", new ArrayList<>());
        res.put("yks", new ArrayList<>());

        for(DeviceItem item : items) {
            int stype = item.getStype().intValue();
            if(ItemStype.TRANSPORTYC.id.intValue() == stype) {
                List<String> values = new ArrayList<>();
                values.add(item.getId());
                values.add(item.getName());
                values.add(item.getShortname());
                values.add(item.getDisplayname());
                values.add(item.getBasic()+"");
                values.add(item.getCtratio()+"");
                values.add(item.getPalarm() + "");
                values.add(item.getStore() + "");
                values.add(item.getBtype() + "");
                res.get("ycs").add(values);

            }else if(ItemStype.TRANSPORTYX.id.intValue() == stype){
                List<String> values = new ArrayList<>();
                values.add(item.getId());
                values.add(item.getName());
                values.add(item.getShortname());
                values.add(item.getDisplayname());
                values.add(item.getDesc0());
                values.add(item.getDesc1());
                values.add(item.getPalarm() + "");
                values.add(item.getBtype() + "");
                res.get("yxs").add(values);

            }else if(ItemStype.TRANSPORTYT.id.intValue() == stype){
                List<String> values = new ArrayList<>();
                values.add(item.getId());
                values.add(item.getName());
                values.add(item.getShortname());
                values.add(item.getType() + "");
                values.add(item.getRatio() + "");
                values.add(item.getBasic() + "");
                values.add(item.getUnit());
                values.add(item.getMaxval() + "");
                values.add(item.getMinval() + "");
                res.get("yts").add(values);

            }else if(ItemStype.TRANSPORTYK.id.intValue() == stype){
                List<String> values = new ArrayList<>();
                values.add(item.getId());
                values.add(item.getName());
                values.add(item.getShortname());
                values.add(item.getDisplayname());
                values.add(item.getDesc());
                res.get("yks").add(values);
            }
        }
        return res;
    }

    @Override
    public Map<String,List<DeviceItem>> getImportItem(List<List<List<String>>> excelData){
        Map<String,List<DeviceItem>> type2Items = new HashMap<>();
        List<DeviceItem> yaoceItems = new ArrayList<>();
        List<List<String>> yaoceSheetData =  excelData.get(0);
        for (List<String> rowData : yaoceSheetData) {
            DeviceItem item = new DeviceItem();
            item.setId(rowData.get(0));
            item.setName(rowData.get(1));
            item.setShortname(rowData.get(2));
            item.setDisplayname(rowData.get(3));
            item.setBasic(Float.valueOf(rowData.get(4)));
            item.setCtratio(Float.valueOf(rowData.get(5)));
            item.setPalarm(Float.valueOf(rowData.get(6)).intValue());
            item.setStore(Float.valueOf(rowData.get(7)).intValue());
            item.setBtype(Float.valueOf(rowData.get(8)).intValue());
            yaoceItems.add(item);
        }
        type2Items.put("YC", yaoceItems);

        List<DeviceItem> yaoxinItems = new ArrayList<>();
        List<List<String>> yaoxinSheetData =  excelData.get(1);
        for (List<String> rowData : yaoxinSheetData) {
            DeviceItem item = new DeviceItem();
            item.setId(rowData.get(0));
            item.setName(rowData.get(1));
            item.setShortname(rowData.get(2));
            item.setDisplayname(rowData.get(3));
            item.setDesc0(rowData.get(4));
            item.setDesc1(rowData.get(5));
            item.setPalarm(Float.valueOf(rowData.get(6)).intValue());
            item.setBtype(Float.valueOf(rowData.get(7)).intValue());
            yaoxinItems.add(item);
        }
        type2Items.put("YX", yaoxinItems);

        List<DeviceItem> yaotiaoItems = new ArrayList<>();
        List<List<String>> yaotiaoSheetData =  excelData.get(2);
        for (List<String> rowData : yaotiaoSheetData) {
            DeviceItem item = new DeviceItem();
            item.setId(rowData.get(0));
            item.setName(rowData.get(1));
            item.setShortname(rowData.get(2));
            item.setType(Float.valueOf(rowData.get(3)).intValue());
            item.setRatio(Float.valueOf(rowData.get(4)));
            item.setBasic(Float.valueOf(rowData.get(5)));
            item.setUnit(rowData.get(6));
            item.setMaxval(Float.valueOf(rowData.get(7)));
            item.setMinval(Float.valueOf(rowData.get(8)));
            yaotiaoItems.add(item);
        }
        type2Items.put("YT", yaotiaoItems);

        List<DeviceItem> yaokongItems = new ArrayList<>();
        List<List<String>> yaokongSheetData =  excelData.get(3);
        for (List<String> rowData : yaokongSheetData) {
            DeviceItem item = new DeviceItem();
            item.setId(rowData.get(0));
            item.setName(rowData.get(1));
            item.setShortname(rowData.get(2));
            item.setDisplayname(rowData.get(3));
            item.setDesc(rowData.get(4));
            yaokongItems.add(item);
        }
        type2Items.put("YK", yaokongItems);
        return type2Items;
    }
}
