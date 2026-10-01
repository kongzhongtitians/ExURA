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
                .executes(context -> executeDefault(context))
                .then(Commands.literal("all_gp")
                        .executes(context -> executeInfo(context)))
                .then(Commands.literal("used_gp")
                        .executes(context -> executeMessage(context)))
                .then(Commands.literal("cheat")
                        .requires(src -> src.hasPermission(3))   // OP3 才能用
                        .then(Commands.literal("true")
                                .executes(ExURACommand::executeTrue))
                        .then(Commands.literal("false")
                                .executes(context -> executeFalse(context)))
                        .then(Commands.literal("set_all_gp_0")
                                .executes(context -> executeo(context)))
                        .then(Commands.literal("set_used_gp_0")
                                .executes(context -> executeot(context)))
                )
        );
    }

    // /exura
    private static int executeDefault(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(
            () -> Component.literal("Extra Utilities Reborn Again Version 1.2.0 RC Edition Copyright (c) 2026 kongzhongtitian The Clear BSD License"), true);
        return Command.SINGLE_SUCCESS;
    }

    // /exura all_gp
    private static int executeInfo(CommandContext<CommandSourceStack> context) {
        GlobalVars g = GlobalVars.getInstance();
        context.getSource().sendSuccess(
            () -> Component.literal(String.valueOf(g.getValue("all_gp"))), false);
        return Command.SINGLE_SUCCESS;
    }

    // /exura used_gp
    private static int executeMessage(CommandContext<CommandSourceStack> context) {
        GlobalVars g = GlobalVars.getInstance();
        context.getSource().sendSuccess(
            () -> Component.literal(String.valueOf(g.getValue("used_gp"))), false);
        return Command.SINGLE_SUCCESS;
    }

    // /exura cheat true → 开作弊（同时改文件）
    private static int executeTrue(CommandContext<CommandSourceStack> context) {
        CheatModeManager.setCheatMode(true);
        context.getSource().sendSuccess(() -> Component.literal("OK"), false);
        return Command.SINGLE_SUCCESS;
    }

    // /exura cheat false → 关作弊（同时改文件）
    private static int executeFalse(CommandContext<CommandSourceStack> context) {
        CheatModeManager.setCheatMode(false);
        context.getSource().sendSuccess(() -> Component.literal("OK"), false);
        return Command.SINGLE_SUCCESS;
    }

    // /exura cheat set_all_gp_0 → all_gp 清零
    private static int executeo(CommandContext<CommandSourceStack> context) {
        GlobalVars g = GlobalVars.getInstance();
        if (g.getValue("cheat_mode") == 1) {
            g.setValue("all_gp", 0);
            context.getSource().sendSuccess(() -> Component.literal("OK"), false);
        } else {
            context.getSource().sendSuccess(() -> Component.literal("You need cheat mode"), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    // /exura cheat set_used_gp_0 → used_gp 清零
    private static int executeot(CommandContext<CommandSourceStack> context) {
        GlobalVars g = GlobalVars.getInstance();
        if (g.getValue("cheat_mode") == 1) {
            g.setValue("used_gp", 0);
            context.getSource().sendSuccess(() -> Component.literal("OK"), false);
        } else {
            context.getSource().sendSuccess(() -> Component.literal("You need cheat mode"), false);
        }
        return Command.SINGLE_SUCCESS;
    }
}
