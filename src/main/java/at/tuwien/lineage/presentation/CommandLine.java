package at.tuwien.lineage.presentation;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;

@ShellComponent
public class CommandLine {

    @ShellMethod
    public String traceForward(@ShellOption List<String> ids) {
        return "";
    }

    @ShellMethod
    public String traceBackward(@ShellOption List<String> ids) {
        return "";
    }

}
