package ie.ul.routeplanning.repositories;

import ie.ul.routeplanning.constants.Constant;
import ie.ul.routeplanning.transport.TransportFactory;
import ie.ul.routeplanning.transport.TransportMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class loads in all available transport methods on system startup.
 *
 * This class is needed to load all transport methods before generating graphs since RouteLegs have a foreign key relationship
 * and the transport methods need to be available before they are created
 */
@Component
public class TransportMethodLoader implements CommandLineRunner {
    /**
     * The repository to dump transport methods into
     */
    private final TransportMethodRepository transportMethodRepository;

    /**
     * Creates an instance of this loader
     * @param transportMethodRepository the repository to dump transport methods into
     */
    @Autowired
    public TransportMethodLoader(TransportMethodRepository transportMethodRepository) {
        this.transportMethodRepository = transportMethodRepository;
    }

    /**
     * Run this component
     * @param args the arguments to pass into the component
     * @throws Exception if an exception occurs
     */
    @Override
    public void run(String... args) throws Exception {
        Set<TransportMethod> loadedTransportMethods =
                new LinkedHashSet<>(Constant.iterableToSet(transportMethodRepository.findAll()));

        List<TransportMethod> transportMethods = new ArrayList<>();

        Arrays.stream(TransportFactory.TransportMethods.values())
                .forEach(m -> transportMethods.add(TransportFactory.getTransportMethod(m)));

        Set<String> names = loadedTransportMethods.stream().map(TransportMethod::getName).collect(Collectors.toSet());
        List<TransportMethod> newTransportMethods = transportMethods.stream()
                .filter(s -> !names.contains(s.getName())).collect(Collectors.toList()); // only save new transport methods

        transportMethodRepository.saveAll(newTransportMethods);
    }
}
