package com.example.conversordemoedas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.conversordemoedas.ui.theme.ConversorDeMoedasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConversorDeMoedasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ConverterScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun ConverterScreen(modifier: Modifier = Modifier) {
    var amountText by rememberSaveable { mutableStateOf("") }
    var brlToUsd by rememberSaveable { mutableStateOf(true) } // true = BRL→USD
    var result by rememberSaveable { mutableStateOf<String?>(null) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    
    val usdPerBrl = 5.30
    val brlPerUsd = 1 / usdPerBrl // 5.0

    fun convert() {
        error = null
        result = null

        val value = amountText.replace(',', '.').toDoubleOrNull()
        if (value == null) {
            error = "Informe um número válido"
            return
        }

        val converted = if (brlToUsd) value * usdPerBrl else value * brlPerUsd
        val from = if (brlToUsd) "BRL" else "USD"
        val to = if (brlToUsd) "USD" else "BRL"
        result = "%.2f %s = %.2f %s".format(value, from, converted, to)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("MoneyFlow", style = MaterialTheme.typography.titleLarge)

        Text(if (brlToUsd) "De: BRL  ›  Para: USD" else "De: USD  ›  Para: BRL")

        OutlinedTextField(
            value = amountText,
            onValueChange = { txt ->
                amountText = txt.filter { it.isDigit() || it == '.' || it == ',' }
            },
            label = { Text("Valor") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )


        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = { brlToUsd = !brlToUsd }) {
                Text("⇄ Inverter")
            }
            Button(onClick = { convert() }) {
                Text("Converter")
            }
        }


        error?.let { msg ->
            Text(msg, color = MaterialTheme.colorScheme.error)
        }

        result?.let { res ->
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = res,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Text(
            "* Taxa Atual: 1 BRL = 5.30 USD.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConverter() {
    ConversorDeMoedasTheme {
        ConverterScreen(Modifier.padding(16.dp))
    }
}
