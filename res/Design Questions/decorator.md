# Decorator Design Pattern
## Part 1: What is Decorator?
The purpose of the decorator design pattern is to recursively wrap a core object in different 'decorator'
objects that enhance the functionality of the core object without changing the interface.
The pattern consists of a base interface, a core object that implements this interface, and a decorator
object that implements the interface, stores a base interface implementing object, and delegates the base
responsibilities to that object. The decorator class can then have any number of children that delegate the
core function to the decorator object.
The Structure implemented simply looks something like this:
``public interface Printable {
public void sayName(String name);
}

        public class Printer implements Printable{
            public void Printer(){
            }
            @Override
            public void sayName(String name){
                System.out.println(name)
            }
        }

        public class printerDecorator implements Printable{
            private Printable wrappee;
            public printerDecorator(Printable wrappee){
                this.wrappee = wrappee;
            }
            @Override
            public void sayName(String name){
                wrappee.sayName(name);
            }
        }

        public class addSmile extends printerDecorator {
            public addSmile(Printable wrappee){
                super(wrappee);
            }
            @Override
            void sayName(String name){
                super.sayName(name + " :)");
            }
        }

        public class addFrown extends printerDecorator {
            public addFrown(Printable wrappee){
                super(wrappee);
            }
            @Override
            void sayName(String name){
                super.sayName(name + " :(");
            }
        }

        public static void main(String[] args){
            Printable p = new Printer();
            Printable f = new addFrown(p);
            Printable s = new addSmiley(p);
            p.sayName("Taite");
            //prints `Taite`
            f.sayName("Taite");
            //prints `Taite :(`
            s.sayName("Taite");
            // prints `Taite :)`
            new addSmiley(f).sayName("Taite");
            // prints `Taite :) :(`
        }``
- Printable is the base interface with a core function sayName that takes a String as a parameter
- basePrinter is the core functional class that implements .sayName(String name) by printing name to
            the screen when called
- printerDecorator is the base decorator class that acts as a container for a Printer, declaring a
            composite relationship with the base interface and delegating the method call .sayName(String name)
            to the Printable object it contains
- addSmile and addFrown are the decorations - the wrappers that delegate the method call .sayName(String name)
            to super and add additional functionality, in this case appending a smile or a frown onto the end (or both
            with recursive wrapping)

## Part 2: Haunted Pyramid Opportunities for Decorator Implementation
The school scheduler has the capability to keep track of objects with a name, a start time and an end
time.
The idea of the actual function of the program besides as a database for these objects is to
automatically create and change collections of these objects in different ways based on user input
and internal scheduling algorithms.

The decorator pattern is used to dynamically add additional functionality to a basic object with a 
set base interface, so if we wanted to implement this pattern (aside from within user preferences for 
Calendar display eventually) it could be useful in the structure of some `Scheduler` Class
which would add simple `Occurence`s to the set of `Occurence`s by a basic `.schedule(occurence)` method, and be decorated
with child classes of a  such as `WeeklyRecursive`, `MonthlyRecursive`, `WorkSessions`.
If we wanted to reduce every scheduler to a basic interface:
```
public interface Schedule {
    Occurence[] schedule(Occurence occurence, EventManager em);
    etc.... 
}
```
we could then define our base `Scheduler` to be one that directly adds a single `Occurence` to the set of `Occurences`
(for us this would be a list in `EventManager`), and a decorator class `FancyScheduler` that also implements this `Schedule`
interface.
```
public class Scheduler implements Schedule{
    public Scheduler(){
    }
    @Override
    public Occurence[] schedule(Occurence occurence, EventManager em){
        em.addEvent(occurence)
        return List.of(occurence);
    }
}
public class FancyScheduler implements Schedule{
    private Schedule wrapped;
    public FancyScheduler(Schedule wrapped){
        this.wrapped = wrapped;
    }
    @Override
    public Occurence[] schedule(Occurence occurence, EventManager em){
        em.addEvent(occurence)
        return List.of(occurence);
    }
}
```
Then we could define any number of `FancyScheduler` Children to take a single `Occurence`
and run a different interaction with the User to obtain information specific to this type of Scheduling, 
Then send it to Managing classes which would create and add to the input `EventManager` a set of 
`Occurences` dependent on the parameters input.

It would take a lot of refactoring to implement this and design a complete structure with decorator Controllers
and Use Case Classes that also allowed for some lightweight permanent pointers between occurences scheduled
together for event removal and editing purposes. Since the objective of a Decorator design class relies on there being 
core functions with set arguments this may not be the most effective design, since each form of scheduling requires
additional input parameters, and each occurence should contain some information on how is was scheduled.


