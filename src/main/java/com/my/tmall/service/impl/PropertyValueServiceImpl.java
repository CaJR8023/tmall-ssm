package com.my.tmall.service.impl;

import com.my.tmall.mapper.PropertyValueMapper;
import com.my.tmall.pojo.Product;
import com.my.tmall.pojo.Property;
import com.my.tmall.pojo.PropertyValue;
import com.my.tmall.pojo.PropertyValueExample;
import com.my.tmall.service.PropertyService;
import com.my.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PropertyValueServiceImpl implements PropertyValueService {
    @Autowired
    PropertyValueMapper propertyValueMapper;
    @Autowired
    PropertyService propertyService;

    @Override
    public void init(Product product) {
        List<Property> pts=propertyService.list(product.getCid());
        for (Property pt:pts){
            PropertyValue propertyValue=get(pt.getId(),product.getId());
                    if(null==propertyValue){
                        propertyValue=new PropertyValue();
                        propertyValue.setPid(product.getId());
                        propertyValue.setPtid(pt.getId());
                        propertyValueMapper.insert(propertyValue);
                    }
        }
    }

    @Override
    public void update(PropertyValue propertyValue) {
        propertyValueMapper.updateByPrimaryKeySelective(propertyValue);
    }

    @Override
    public PropertyValue get(int ptid,int pid) {
        PropertyValueExample example=new PropertyValueExample();
        example.createCriteria().andPtidEqualTo(ptid)
                .andPidEqualTo(pid);
        List<PropertyValue> pvs=propertyValueMapper.selectByExample(example);
        if(pvs.isEmpty())
        return null;
        return pvs.get(0);
    }

    @Override
    public List<PropertyValue> list(int pid) {
        PropertyValueExample example=new PropertyValueExample();
        example.createCriteria().andPidEqualTo(pid);
        List<PropertyValue> result=propertyValueMapper.selectByExample(example);
        for (PropertyValue pv:result){
            Property property=propertyService.get(pv.getPtid());
            pv.setProperty(property);
        }
        return result;
    }
}
