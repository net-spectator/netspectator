package services;

import entities.Device;
import entities.DeviceGroup;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


import javax.persistence.NoResultException;
import javax.transaction.Transactional;

@Transactional
public class DataBaseService {
    private static final Logger LOGGER = Logger.getLogger(DataBaseService.class);

    SessionFactory factory = new Configuration() //удалить
            .configure("hibernate.cfg.xml")
//            .addAnnotatedClass(Device.class)
            .buildSessionFactory();
    Session session = null;

    //возвращает полный список устройств со статусами
    public static String[] getDeiceList() {

        return null;
    }

    //возвращает список устройств со статусами по определенной группе
    public static String[] getDeviceListByGroup(String groupName) {

        return null;
    }

    //добавляет новое устройство в базу данных
    public int addDevice(Device device) {
        session = factory.getCurrentSession();
        session.beginTransaction();
        DeviceGroup deviceGroup = (DeviceGroup) session.createQuery("from DeviceGroup where title=:title")
                .setParameter("title","main").getSingleResult();
        device.setDeviceGroup(deviceGroup);
        session.save(device);
        session.getTransaction().commit();
        return -1;
    }

    public Device getDeviceByUUID(String uuid) {
        session = factory.getCurrentSession();
        session.beginTransaction();
        Device device = null;
        try {
            device = (Device) session.createQuery("from Device where UUID=:uuid").setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException e) {
            session.getTransaction().commit();
            return null;
        }
        session.getTransaction().commit();
        return device;
    }

    public void changeDeviceStatus(String uuid, boolean status) {
        session = factory.getCurrentSession();
        session.beginTransaction();
        Device device = null;
        try {
            device = (Device) session.createQuery("from Device where UUID=:uuid").setParameter("uuid", uuid).getSingleResult();
            device.setOnlineStatus(status ? 1 : 0);
        } catch (NoResultException e) {
            session.getTransaction().commit();
            return;
        }
        session.getTransaction().commit();
    }

    //удаляет выбранное устройство из базы данных
    public static int deleteDevice(Device device) {

        return -1;
    }

    //изменяет статус устройства (мне кажется этот метод не понадобиться
    //так как после подключения hibernate наблюдает за объектами
    public static int changeDeviceStatus(Device device) {

        return -1;
    }

}
