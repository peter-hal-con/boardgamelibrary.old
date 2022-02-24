package com.halcon.gaming.boardgamelibrary.cucumber

import static org.awaitility.Awaitility.*

import java.util.concurrent.TimeUnit

class ProcessManager {
    final String cmdline
    final File directory
    final Closure isRunning

    Process process
    StringBuilder sout
    StringBuilder serr

    public ProcessManager(String cmdline, File directory, Closure isRunning) {
        this.cmdline = cmdline
        this.directory = directory
        this.isRunning = isRunning
    }

    public boolean start() {
        sout = new StringBuilder()
        serr = new StringBuilder()
        process = cmdline.execute(null, directory)
        process.consumeProcessOutput(sout, serr)

        await().atMost(5, TimeUnit.MINUTES).until {
            if(!process.alive) {
                println('--- COMMAND FAILED ---')
                println(cmdline)
                if(process.isAlive()) {
                    println("Process was RUNNING");
                    process.waitForOrKill(10000)
                } else {
                    println("Process was TERMINATED");
                }
                println()
                println('Standard Output:')
                println(sout)
                println('Standard Error:')
                println(serr)
                println('----------------------')
            }
            return isRunning()
        }

        return true
    }

    private static terminate(ProcessHandle handle) {
        ProcessHandle[] children = handle.children().toArray()
        while(children.length > 0) {
            children.each { terminate(it) }
            children = handle.children().toArray()
        }
        if(handle.isAlive()) {
            handle.destroy()
        }
        try {
            await().atMost(5, TimeUnit.SECONDS).until { !handle.alive }
        } catch(Exception e) {
        }
        if(handle.isAlive()) {
            handle.destroyForcibly()
        }
        try {
            await().atMost(5, TimeUnit.SECONDS).until { !handle.alive }
        } catch(Exception e) {
        }
    }

    public boolean stop() {
        terminate(process.toHandle())
    }
}
