package cat.paucasesnovescifp.test_spdvi;

import java.beans.*;

public class FileUploadComponentBeanInfo extends SimpleBeanInfo {
    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor connectionStringDesc = 
                new PropertyDescriptor("connectionString", FileUploadComponent.class);
            PropertyDescriptor containerNameDesc = 
                new PropertyDescriptor("containerName", FileUploadComponent.class);
            
            return new PropertyDescriptor[] { 
                connectionStringDesc, 
                containerNameDesc 
            };
        } catch (IntrospectionException e) {
            return null;
        }
    }
}
