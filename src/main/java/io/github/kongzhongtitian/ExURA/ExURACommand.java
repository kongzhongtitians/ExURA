package io.github.kongzhongtitian.ExURA;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ExURACommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("exura")
                .executes(context -> {
                    // 当只输入 /test 时执行
                    return executeDefault(context);
                })
                .then(Commands.literal("all_gp")
                        .executes(context -> {
                            // /test info
                            return executeInfo(context);
                        })
                )
                .then(Commands.literal("used_gp")
                        .executes(context -> {
                            // /test message <消息>
                            return executeMessage(context);
                        })
                )
                .then(Commands.literal("cheat")
                        .requires(src -> src.hasPermission(3))
                        .then(Commands.literal("true")
                                .executes(ExURACommand::executeTrue
                                        )
                        )
                        .then(Commands.literal("false")
                                .executes(context -> {
                                    // /test message <消息>
                                    return executeFalse(context);
                                })
                        )
                        .then(Commands.literal("set_all_gp_0")
                                .executes(context -> {
                                    // /test message <消息>
                                    return executeo(context);
                                })
                        )
                        .then(Commands.literal("set_used_gp_0")
                                .executes(context -> {
                                    // /test message <消息>
                                    return executeot(context);
                                })
                        )
                )
        );
    }

    private static int executeDefault(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        source.sendSuccess(() -> Component.literal("Extra Utilities Reborn Again"), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int executeInfo(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        GlobalVars globals = GlobalVars.getInstance();
        source.sendSuccess(() -> Component.literal(String.valueOf(globals.getValue("all_gp"))), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int executeMessage(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        GlobalVars globals = GlobalVars.getInstance();
        source.sendSuccess(() -> Component.literal(String.valueOf(globals.getValue("used_gp"))), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int executeTrue(CommandContext<CommandSourceStack> context) {
        CheatModeManager.setCheatMode(true);
        context.getSource().sendSuccess(() -> Component.literal("OK"), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int executeFalse(CommandContext<CommandSourceStack> context) {
        CheatModeManager.setCheatMode(false);
        context.getSource().sendSuccess(() -> Component.literal("OK"), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int executeo(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        GlobalVars globals = GlobalVars.getInstance();
        if (globals.getValue("cheat_mode")==1){
            globals.setValue("all_gp",0);
            source.sendSuccess(() -> Component.literal("OK"), false);
        }else {
            source.sendSuccess(() -> Component.literal("Noooooooooooooooooo!"), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int executeot(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        GlobalVars globals = GlobalVars.getInstance();
        if (globals.getValue("cheat_mode")==1){
            globals.setValue("used_gp",0);
            source.sendSuccess(() -> Component.literal("OK"), false);
        }else {
            source.sendSuccess(() -> Component.literal("Noooooooooooooooooo!"), false);
        }
        return Command.SINGLE_SUCCESS;
    }
}
