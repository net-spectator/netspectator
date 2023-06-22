package services;

import entities.TrackedEquipment;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


import javax.persistence.EntityGraph;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
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
    public static String[] getTrackedEquipmentList() {

        return null;
    }

    //возвращает список устройств со статусами по определенной группе
    public static String[] getTrackedEquipmentListByGroup(String groupName) {

        return null;
    }

    //добавляет новое устройство в базу данных
    public int addTrackedEquipment(TrackedEquipment device) {
        session = factory.getCurrentSession();
        session.beginTransaction();
        session.save(device);
        session.getTransaction().commit();
        return -1;
    }
    @Transactional
    public TrackedEquipment getTrackedEquipmentByUUID(String uuid) {
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

    public void changeDeviceStatus(String uuid, boolean status) {
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

    public void updateTrackedEquipment(TrackedEquipment device){
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
    public static int changeTrackedEquipmentStatus(TrackedEquipment device) {

        return -1;
    }

}
