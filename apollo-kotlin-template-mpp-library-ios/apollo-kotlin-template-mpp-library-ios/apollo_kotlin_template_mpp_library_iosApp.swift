//
//  apollo_kotlin_template_mpp_library_iosApp.swift
//  apollo-kotlin-template-mpp-library-ios
//
//  Created by BoD on 01/07/2022.
//

import SwiftUI
import MyMppLibrary

@main
struct apollo_kotlin_template_mpp_library_iosApp: App {
    init() {
        let myMppLibrary = MyMppLibrary()
        
        // Use `Task {` to be on the main thread and `Task.detached {` to be on a background thread
        Task {
            print(Thread.isMainThread)
            print("Launching")
            KotlinNativeFlowWrapper<LaunchListQuery.Launches>(flow: myMppLibrary.executeSampleFlowQuery())
                .subscribe(onEach: { result in debugPrint(result ?? "No results") }, onComplete: { print("COMPLETE") }, onThrow: { error in debugPrint(error) })
            
            // Show normalized cache contents
            let normalizedCacheStr = try await myMppLibrary.getNormalizedCacheString()
            print("\nNormalized cache contents:")
            print(normalizedCacheStr)
        }
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

class CollectPrinter: Kotlinx_coroutines_coreFlowCollector {
    private var counter = 0
    func emit(value: Any?) async throws {
        counter += 1
        print("Response # \(counter)")
        debugPrint(value ?? "Nothing")
    }
}
