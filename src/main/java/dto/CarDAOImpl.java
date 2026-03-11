package dto;

import dao.DAOImpl;

public class CarDAOImpl extends DAOImpl<Car> implements CarDAO {
    public CarDAOImpl(Class<Car> tclass) {
        super(tclass);
    }
}
