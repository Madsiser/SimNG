package simulation.engine.commands;

public record Command(CommandType type, Object data) {

    @Override
    public String toString() {
        return "Command{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }
}
