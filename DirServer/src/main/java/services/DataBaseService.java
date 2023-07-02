package services;

import entities.TrackedEquipment;
import enums.Status;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import utils.NSLogger;


import javax.persistence.NoResultException;
import javax.transaction.Transactional;

@Transactional
public class DataBaseService {
    private static final NSLogger LOGGER = new NSLogger(DataBaseService.class);


    private static final SessionFactory factory;
    private static Session session = null;

    static {
        factory = new Configuration() // TODO: 23.06.2023 обеспечить доступ к этому классу из других классов
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }

    //возвращает полный список устройств со статусами
    public static String[] getTrackedEquipmentList() {

        return null;
    }

    //добавляет новое устройство в базу данных
    public static synchronized int addTrackedEquipment(TrackedEquipment device) {
        session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            session.save(device);
        } catch (Exception e) {
            return -1;
        }
        session.getTransaction().commit();
        return 1;
    }

    public static synchronized TrackedEquipment getTrackedEquipmentByUUID(String uuid) {
        session = factory.getCurrentSession();
        session.beginTransaction();
        TrackedEquipment device = null;
        try {
            device = (TrackedEquipment) session.createQuery("from TrackedEquipment where equipmentUuid=:uuid").setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException e) {
            session.getTransaction().commit();
            return null;
        }
        session.getTransaction().commit();
        return device;
    }

    public static synchronized void changeDeviceStatus(String uuid, boolean status) {
        session = factory.getCurrentSession();
        session.beginTransaction();
        TrackedEquipment device = null;
        try {
            device = (TrackedEquipment) session.createQuery("from TrackedEquipment where equipmentUuid=:uuid").setParameter("uuid", uuid).getSingleResult();
            device.setEquipmentOnlineStatus(status ? 1 : 0);
        } catch (NoResultException e) {
            session.getTransaction().commit();
            return;
        }
        session.getTransaction().commit();
    }

    public static synchronized void updateTrackedEquipment(TrackedEquipment device) {
        session = factory.getCurrentSession();
        session.beginTransaction();
        session.update(device);
        session.getTransaction().commit();
    }

    //удаляет выбранное устройство из базы данных
    public static int deleteTrackedEquipment(TrackedEquipment device) {

        return -1;
    }

    //изменяет статус устройства (мне кажется этот метод не понадобиться
    //так как после подключения hibernate наблюдает за объектами
    public static void changeTrackedEquipmentStatus(long device_id, Status status) {

    }

}
