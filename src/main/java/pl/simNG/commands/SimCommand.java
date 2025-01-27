package pl.simNG.commands;

public record SimCommand(SimCommandType type, Object data) {

    @Override
    public String toString() {
        return "Command{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }
}
