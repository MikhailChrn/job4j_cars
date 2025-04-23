package ru.job4j.cars.entity.carcomponents;

import jakarta.persistence.*;
import lombok.*;
import ru.job4j.cars.entity.carhistory.CarOwnerHistory;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
@Builder
@Getter
@Setter
@EqualsAndHashCode(of = {"id", "title"})
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    /**
     * Ссылка на марку автомобиля
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_brand_id")
    private CarBrand carBrand;

    /**
     * Ссылка на тип кузова
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_type_id")
    private BodyType bodyType;

    /**
     * Ссылка на марку двигателя
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engine_id")
    private Engine engine;

    /**
     * Хранение хронологии собственников у автомобиля
     */
    @OneToMany(mappedBy = "car",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<CarOwnerHistory> carOwnerHistories = new ArrayList<>();

    public void addCarOwnerHistory(CarOwnerHistory carOwnerHistory) {
        carOwnerHistory.setCar(this);
        this.carOwnerHistories.add(carOwnerHistory);
    }

    public void removeCarOwnerHistory(CarOwnerHistory carOwnerHistory) {
        carOwnerHistory.setCar(null);
        this.carOwnerHistories.remove(carOwnerHistory);
    }

    public Car(String title, CarBrand carBrand) {
        this.title = title;
        this.carBrand = carBrand;
    }

    public Car(String title, CarBrand carBrand, BodyType bodyType, Engine engine) {
        this.title = title;
        this.carBrand = carBrand;
        this.bodyType = bodyType;
        this.engine = engine;
    }

    /**
     * У двусторонних отношений помимо стороны - владельца (owning side) имеется ещё
     * и противоположная сторона (inverse side).
     * Т.е. обе стороны отношения обладают информацией о связи.
     * Логично предположить, что из одностороннего отношения можно сделать двустороннее
     * просто добавив поле и аннотацию в класс сущности противоположной стороны, но не все так просто.
     *
     * Дело в том, что вместо одного двустороннего отношения мы с вами сейчас создали два односторонних.
     * Тоже самое произойдет и для отношения один ко многим.
     * Чтобы Hibernate понял, что мы хотим создать именно двустороннее отношение нам нужно указать,
     * какая из сторон является владельцем отношений, а какая является обратной стороной.
     *
     * Это делается при помощи атрибута mappedBy.
     * Важно отметить, что указывается этот атрибут в аннотации,
     * которая находится на противоположной стороне отношения.
     *
     * !!!! значение атрибута mappedBy - имя поля связи в классе сущности-владельца отношений !!!!
     *
     * mappedBy = "team" говорит о том, что связь между Player и Team осуществляется через поле team.
     * Коллекция players в классе Team отражает структуру базы данных и точно описывает взаимосвязи объектов.
     *
     * !!!! В JPA и Hibernate владельцем связи признаётся та сторона, где установлен внешний ключ, и не указывается атрибут mappedBy.
     * В контексте отношения "Многие к одному" владельцем выступает сторона Player. Аннотация @JoinColumn определяет связанный столбец базы данных.
     * !!!!
     *
     * !!!! Вспомогательные методы служат для синхронизации состояний двунаправленной связи: !!!!
     */
}
