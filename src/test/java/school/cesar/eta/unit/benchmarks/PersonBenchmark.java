package school.cesar.eta.unit.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import school.cesar.eta.unit.Person;
import school.cesar.eta.unit.CpfValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * JMH Benchmarks for Person class operations.
 *
 * <p>
 * This class measures the performance of critical operations in the Person class including family management, age
 * calculations, and CPF validation.
 *
 * <p>
 * To run these benchmarks:
 *
 * <pre>{@code
 * mvn clean install
 * java -jar target/benchmarks.jar
 * }</pre>
 *
 * @author CESAR School
 * @since 1.0
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = { "-Xms2G", "-Xmx2G" })
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
public class PersonBenchmark {

    @Param({ "10", "100", "1000" })
    private int familySize;

    private Person mainPerson;
    private List<Person> familyMembers;
    private List<String> validCpfs;
    private List<String> invalidCpfs;

    @Setup
    public void setup() {
        // Setup main person
        mainPerson = new Person();
        mainPerson.setId(0L);
        mainPerson.setFirstName("Main");
        mainPerson.setLastName("Person");
        mainPerson.setBirthday(LocalDate.of(1990, 1, 1));

        // Setup family members
        familyMembers = new ArrayList<>();
        for (int i = 0; i < familySize; i++) {
            Person member = new Person();
            member.setId((long) (i + 1));
            member.setFirstName("Member" + i);
            member.setLastName("Family");
            member.setBirthday(LocalDate.of(1990 + (i % 30), 1, 1));
            familyMembers.add(member);
        }

        // Setup CPF lists
        validCpfs = new ArrayList<>();
        invalidCpfs = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            validCpfs.add(CpfValidator.generateRandom());
            invalidCpfs.add("111.111.111-11");
        }
    }

    @Benchmark
    public void benchmarkAddToFamily() {
        Person person = new Person();
        person.setId(9999L);
        for (Person member : familyMembers) {
            person.addToFamily(member);
        }
    }

    @Benchmark
    public boolean benchmarkIsFamily() {
        // Add all family members first
        for (Person member : familyMembers) {
            mainPerson.addToFamily(member);
        }

        // Benchmark checking if last member is family
        return mainPerson.isFamily(familyMembers.get(familyMembers.size() - 1));
    }

    @Benchmark
    public List<Person> benchmarkGetFamily() {
        // Add all family members first
        for (Person member : familyMembers) {
            mainPerson.addToFamily(member);
        }

        return mainPerson.getFamily();
    }

    @Benchmark
    public Integer benchmarkGetAge() {
        return mainPerson.getAge();
    }

    @Benchmark
    public Integer benchmarkGetAgeInMonths() {
        return mainPerson.getAgeInMonths();
    }

    @Benchmark
    public Long benchmarkGetAgeInDays() {
        return mainPerson.getAgeInDays();
    }

    @Benchmark
    public boolean benchmarkCpfValidation() {
        boolean result = true;
        for (String cpf : validCpfs) {
            result &= CpfValidator.isValid(cpf);
        }
        return result;
    }

    @Benchmark
    public boolean benchmarkInvalidCpfValidation() {
        boolean result = false;
        for (String cpf : invalidCpfs) {
            result |= CpfValidator.isValid(cpf);
        }
        return result;
    }

    @Benchmark
    public String benchmarkCpfFormatting() {
        StringBuilder result = new StringBuilder();
        for (String cpf : validCpfs) {
            result.append(CpfValidator.format(cpf));
        }
        return result.toString();
    }

    @Benchmark
    public String benchmarkGetName() {
        return mainPerson.getName();
    }

    @Benchmark
    public boolean benchmarkIsBirthdayToday() {
        return mainPerson.isBirthdayToday();
    }

    /**
     * Main method to run benchmarks standalone.
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(PersonBenchmark.class.getSimpleName()).forks(1).build();

        new Runner(opt).run();
    }
}